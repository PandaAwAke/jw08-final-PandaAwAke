package com.pandaawake.gourdgame.network;

import com.mandas.tiled2d.core.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

// From https://github.com/arukshani/JavaIOAndNIO
/**
 *
 * Test client for NIO server
 *
 */
public class Client {
    private InetSocketAddress hostAddress;
    private SocketChannel channel;

    public Client() throws IOException {
        hostAddress = new InetSocketAddress("localhost", 9093);
        channel = SocketChannel.open(hostAddress);
    }

    public void clientOperation() throws IOException, InterruptedException {
        String threadName = Thread.currentThread().getName();

        // Send messages to server
        String[] messages = new String[]{threadName + ": msg1", threadName + ": msg2", threadName + ": msg3"};

        for (int i = 0; i < messages.length; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(74);
            buffer.put(messages[i].getBytes());
            buffer.flip();
            channel.write(buffer);
            System.out.println(messages[i]);
            buffer.clear();
            Thread.sleep(5000);
        }

        channel.close();
    }




    public static class ClientRunnable implements Runnable {

        private Client client;

        public ClientRunnable() {
            try {
                client = new Client();
            } catch (IOException e) {
                Log.app().error("Exception when creating Client!");
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
//            while (true) {
//                try {
//
//                }
//                catch () {
//                    Log.app().error("IOException when running GameServer!");
//                    e.printStackTrace();
//                }
//            }
        }
    }

}
