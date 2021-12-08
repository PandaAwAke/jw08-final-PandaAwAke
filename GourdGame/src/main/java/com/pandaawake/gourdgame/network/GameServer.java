package com.pandaawake.gourdgame.network;

import com.mandas.tiled2d.core.Log;

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
public class GameServer {
    private Selector selector;

    private final static int PORT = 9093;
    private final Set<SocketChannel> clientChannels;
    private final Map<byte[], Set<SocketChannel>> dataToWrite_SendedChannels;

    private boolean running = true;


    public GameServer(String address) throws IOException {
        InetSocketAddress listenAddress = new InetSocketAddress(address, PORT);
        clientChannels = new HashSet<>();
        dataToWrite_SendedChannels = new HashMap<>();

        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // bind server socket channel to port
        serverChannel.socket().bind(listenAddress);
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

        Log.app().info("GameServer started on port >> " + PORT);
    }

    Selector getSelector() {
        return selector;
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
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
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    // read from the socket channel
    void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int numRead = -1;
        numRead = channel.read(buffer);

        if (numRead == -1) {
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            Log.app().info("Connection closed by client: " + remoteAddr);
            channel.close();
            // channel.shutdownInput();
            key.cancel();
            return;
        }

        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);

        Log.app().info("Got: " + new String(data));
    }

    void write(SelectionKey key, byte[] data) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
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



    public void addDataToWrite(byte[] dataToWrite) {
        dataToWrite_SendedChannels.put(dataToWrite, new HashSet<>());
    }


    byte[] SendClientData() {
        return null;
    }


    public static class GameServerRunnable implements Runnable {

        private GameServer gameServer;

        public GameServerRunnable() {
            try {
                gameServer = new GameServer("localhost");
            } catch (IOException e) {
                Log.app().error("IOException when creating GameServer!");
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (gameServer.isRunning()) {
                try {
                    // wait for events
                    int readyCount = gameServer.getSelector().select();
                    if (readyCount == 0) {
                        continue;
                    }

                    // process selected keys...
                    Set<SelectionKey> readyKeys = gameServer.getSelector().selectedKeys();
                    Iterator<SelectionKey> iterator = readyKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = (SelectionKey) iterator.next();

                        // Remove key from set so we don't process it twice
                        iterator.remove();

                        if (!key.isValid()) {
                            continue;
                        }

                        if (key.isAcceptable()) { // Accept client connections
                            gameServer.accept(key);
                        } else if (key.isReadable()) { // Read from client
                            gameServer.read(key);
                        } else if (key.isWritable()) {
                            // write data to client...
                        }
                    }
                } catch (IOException e) {
                    Log.app().error("IOException when running GameServer!");
                    e.printStackTrace();
                }
            }
        }
    }
}
