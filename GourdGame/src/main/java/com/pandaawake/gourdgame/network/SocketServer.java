package com.pandaawake.gourdgame.network;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.network.data.DataProcessor;
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

    private final Set<SocketChannel> clientChannels;
    private final Map<byte[], Set<SocketChannel>> dataToWrite_SendedChannels;
    private final LinkedList<byte[]> dataRead;

    public SocketServer() {
        clientChannels = new HashSet<>();
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
            System.exit(1);
        }
    }

    // accept client connection
    void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        Log.app().info("Connected to: " + remoteAddr);
        clientChannels.add(channel);

        /*
         * Register channel with selector for further IO (record it for read/write
         * operations, here we have used read operation)
         */
        channel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    // read from the socket channel
    void read(SelectionKey key) throws IOException {
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
        // Log.app().info("Got: " + new String(oStream.toByteArray()));
        dataRead.offer(oStream.toByteArray());
    }

    void write(SocketChannel channel, byte[] data) throws IOException {
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

    void processDataToWrite(SelectionKey key) throws IOException {
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

    // ------------------- Public functions -------------------
    public void addDataToWrite(byte[] dataToWrite) {
        dataToWrite_SendedChannels.put(dataToWrite, new HashSet<>());
    }

    public boolean hasDataToHandle() {
        return !dataRead.isEmpty();
    }

    public byte[] pollDataToHandle() {
        return dataRead.poll();
    }

    public void closeConnection(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        Log.app().info("Connection closed by client: " + remoteAddr);
        channel.close();
        key.cancel();
    }

    public void run() {
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
                SelectionKey key = (SelectionKey) iterator.next();

                // Remove key from set so we don't process it twice
                iterator.remove();

                if (!key.isValid()) {
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

    public void close() {
        try {
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                // Remove key from set so we don't process it twice
                iterator.remove();
                if (!key.isValid()) {
                    continue;
                }
                SocketChannel channel = (SocketChannel) key.channel();
                if (key.isWritable()) { // Write to client
                    write(channel, UtilFunctions.intToBytes(DataProcessor.ServerClosed));
                }
                channel.close();
                key.cancel();
            }
        } catch (IOException e) {
            Log.app().fatal(this.getClass().getName() + ": IOException when closing SocketServer!");
            e.printStackTrace();
        }
    }

}
