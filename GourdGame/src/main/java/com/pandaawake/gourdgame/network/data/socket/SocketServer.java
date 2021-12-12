package com.pandaawake.gourdgame.network.data.socket;

import com.mandas.tiled2d.core.Log;
import com.mandas.tiled2d.utils.Pair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.network.data.data.DataProcessor;
import com.pandaawake.gourdgame.utils.DataUtils;

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
    private final LinkedList<Pair<byte[], Set<SocketChannel>>> dataToWrite_SendedChannels;
    private final LinkedList<Pair<Integer, byte[]>> dataRead;
    private int clientIdIter = 0;

    private int getChannelIndex(SocketChannel channel) {
        for (Map.Entry<Integer, SocketChannel> entry : clientChannels.entrySet()) {
            if (channel.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public SocketServer() {
        clientChannels = new HashMap<>();
        dataToWrite_SendedChannels = new LinkedList<>();
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
            ByteBuffer buffer = ByteBuffer.allocate(4096);

            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            int off = 0;
            int numRead = channel.read(buffer);

            while (numRead != 0 && numRead != -1) {
                oStream.write(buffer.array(), off, numRead);
                off += numRead;
                numRead = channel.read(buffer);
            }

            if (numRead == -1) {
                // TODO: Server closed
            }

            if (oStream.size() > 0) {
                dataRead.offer(new Pair<>(getChannelIndex(channel), oStream.toByteArray()));
            }
        }
    }

    void write(SocketChannel channel, byte[] data) throws IOException {
        synchronized (this) {
            int numWrite = 0;
            while (numWrite < data.length) {
                ByteBuffer buffer = ByteBuffer.allocate(4096);
                int writeLen = Math.min(4096, data.length - numWrite);
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
            Set<Pair<byte[], Set<SocketChannel>>> entriesToRemove = new HashSet<>();
            for (Pair<byte[], Set<SocketChannel>> entry : dataToWrite_SendedChannels) {
                if (!entry.second.contains(channel)) {
                    write(channel, entry.first);
                    entry.second.add(channel);
                    if (entry.second.size() == clientChannels.size()) {
                        // Remove this entry
                        entriesToRemove.add(entry);
                    }
                }
            }
            for (Pair<byte[], Set<SocketChannel>> entry : entriesToRemove) {
                dataToWrite_SendedChannels.remove(entry);
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
            dataToWrite_SendedChannels.offer(new Pair<>(dataToWrite, new HashSet<>()));
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
            dataToWrite_SendedChannels.offer(new Pair<>(dataToWrite, ignoreSet));
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

    public Pair<Integer, byte[]> pollDataToHandle() {
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
                        write(channel, DataUtils.intToBytes(DataProcessor.SERVER_CLOSED));
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
