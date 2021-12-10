package com.pandaawake.gourdgame.network.data.socket;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.network.data.data.DataProcessor;
import com.pandaawake.gourdgame.utils.UtilFunctions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

// From https://github.com/arukshani/JavaIOAndNIO
/**
 *
 * This is a simple NIO based server.
 *
 */
public class SocketServer {

    private Selector selector;

    private final Map<Integer, SocketChannel> clientChannels;
    private final Map<byte[], Set<SocketChannel>> dataToWrite_SendedChannels;
    private final LinkedList<byte[]> dataRead;
    private int clientIdIter = 0;

    public SocketServer() {
        clientChannels = new HashMap<>();
        dataToWrite_SendedChannels = new HashMap<>();
        dataRead = new LinkedList<>();

        try {
            InetSocketAddress listenAddress = new InetSocketAddress(Config.ServerHostName, Config.ServerPort);

            this.selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);

            // bind server socket channel to port
            serverChannel.socket().bind(listenAddress);
            serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

            Log.app().info("SocketServer started on port >> " + Config.ServerPort);

        } catch (IOException e) {
            Log.app().fatal(this.getClass().getName() + ": IOException when creating SocketServer!");
            e.printStackTrace();

        }
    }

    // accept client connection
    void accept(SelectionKey key) throws IOException {
        synchronized (this) {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel channel = serverChannel.accept();
            channel.configureBlocking(false);
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            Log.app().info("Connected to: " + remoteAddr);
            clientChannels.put(clientIdIter++, channel);

            /*
             * Register channel with selector for further IO (record it for read/write
             * operations, here we have used read operation)
             */
            channel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
    }

    // read from the socket channel
    void read(SelectionKey key) throws IOException {
        synchronized (this) {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int numRead = -1;

            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            int off = 0;
            while ((numRead = channel.read(buffer)) != -1) {
                // assert : data.length <= 1024
                oStream.write(buffer.array(), off, numRead);
                off += numRead;
            }
            dataRead.offer(oStream.toByteArray());
        }
    }

    void write(SocketChannel channel, byte[] data) throws IOException {
        synchronized (this) {
            int numWrite = 0;
            while (numWrite < data.length) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int writeLen = Math.min(1024, data.length - numWrite);
                buffer.put(data, numWrite, writeLen);
                numWrite += writeLen;
                buffer.flip();
                channel.write(buffer);
                buffer.clear();
            }
        }
    }

    void processDataToWrite(SelectionKey key) throws IOException {
        synchronized (this) {
            SocketChannel channel = (SocketChannel) key.channel();
            Set<byte[]> bytesToRemove = new HashSet<>();
            for (Map.Entry<byte[], Set<SocketChannel>> entry : dataToWrite_SendedChannels.entrySet()) {
                if (!entry.getValue().contains(channel)) {
                    write(channel, entry.getKey());
                    entry.getValue().add(channel);
                    if (entry.getValue().size() == clientChannels.size()) {
                        // Remove this entry
                        bytesToRemove.add(entry.getKey());
                    }
                }
            }
            for (byte[] bytes : bytesToRemove) {
                dataToWrite_SendedChannels.remove(bytes);
            }
        }
    }

    void closeConnection(SelectionKey key) throws IOException {
        synchronized (this) {
            SocketChannel channel = (SocketChannel) key.channel();
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            Log.app().info("Connection closed by client: " + remoteAddr);
            channel.close();
            key.cancel();

            int matchedClientId = -1;
            for (Map.Entry<Integer, SocketChannel> entry : clientChannels.entrySet()) {
                if (entry.getValue().equals(channel)) {
                    matchedClientId = entry.getKey();
                    break;
                }
            }

            if (matchedClientId == -1) {
                Log.app().error(getClass().getName() + ": No matched client id??");
            }

            clientChannels.remove(matchedClientId);
        }
    }

    // ------------------- Public functions -------------------
    public void addDataToWrite(byte[] dataToWrite) {
        synchronized (this) {
            dataToWrite_SendedChannels.put(dataToWrite, new HashSet<>());
        }
    }

    public void addDataToWrite(byte[] dataToWrite, Collection<Integer> ignoreList) {
        synchronized (this) {
            Set<SocketChannel> ignoreSet = new HashSet<>();
            for (Integer ignoreId : ignoreList) {
                SocketChannel channel = clientChannels.get(ignoreId);
                if (channel == null) {
                    Log.app().error(getClass().getName() + ": wrong id of channel!");
                }
                ignoreSet.add(channel);
            }
            dataToWrite_SendedChannels.put(dataToWrite, ignoreSet);
        }
    }

    public void addDataToWrite(byte[] dataToWrite, int ignoreId) {
        Collection<Integer> ignoreList = new HashSet<>();
        ignoreList.add(ignoreId);
        addDataToWrite(dataToWrite, ignoreList);
    }

    public Set<Integer> getClientIds() {
        return clientChannels.keySet();
    }

    public boolean hasDataToHandle() {
        synchronized (this) {
            return !dataRead.isEmpty();
        }
    }

    public byte[] pollDataToHandle() {
        synchronized (this) {
            return dataRead.poll();
        }
    }

    public void run() {
        synchronized (this) {
            try {
                // wait for events
                int readyCount = selector.select();
                if (readyCount == 0) {
                    return;
                }

                // process selected keys...
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = readyKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    // Remove key from set so we don't process it twice
                    iterator.remove();

                    if (!key.isValid()) {
                        closeConnection(key);
                        continue;
                    }

                    if (key.isAcceptable()) { // Accept client connections
                        accept(key);
                    } else {
                        if (key.isReadable()) { // Read from client
                            read(key);
                        }
                        if (key.isWritable()) { // Write to client
                            processDataToWrite(key);
                        }
                    }
                }
            } catch (IOException e) {
                Log.app().fatal(this.getClass().getName() + ": IOException when running SocketServer!");
                e.printStackTrace();
            }
        }
    }

    public void close() {
        synchronized (this) {
            try {
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = readyKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    // Remove key from set so we don't process it twice
                    iterator.remove();
                    if (!key.isValid()) {
                        closeConnection(key);
                        continue;
                    }
                    SocketChannel channel = (SocketChannel) key.channel();
                    if (key.isWritable()) { // Write to client
                        write(channel, UtilFunctions.intToBytes(DataProcessor.SERVER_CLOSED));
                    }
                    channel.close();
                    key.cancel();
                }
                clientChannels.clear();
            } catch (IOException e) {
                Log.app().fatal(this.getClass().getName() + ": IOException when closing SocketServer!");
                e.printStackTrace();
            }
        }
    }

}
