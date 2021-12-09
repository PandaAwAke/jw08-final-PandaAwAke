package com.pandaawake.gourdgame.network;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.Config;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;


// From https://github.com/arukshani/JavaIOAndNIO
/**
 *
 * Test client for NIO server
 *
 */

public class SocketClient {

    private final Map<byte[], Set<SocketChannel>> dataToWrite_SendedChannels;
    private final LinkedList<byte[]> dataRead;
    
    private InetSocketAddress hostAddress;
    private SocketChannel channel;

    public SocketClient() {
        hostAddress = new InetSocketAddress(Config.ServerHostName, Config.ServerPort);

        try {
            channel = SocketChannel.open(hostAddress);
        } catch (IOException e) {
            Log.app().fatal(this.getClass().getName() + ": IOException when creating SocketClient!");
            e.printStackTrace();
            System.exit(1);
        }

        dataToWrite_SendedChannels = new HashMap<>();
        dataRead = new LinkedList<>();
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
    }

    public void run() {
        
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

    public void close() {
        try {
            channel.close();
        } catch (IOException e) {
            Log.app().fatal(this.getClass().getName() + ": IOException when closing SocketClient!");
            e.printStackTrace();
            System.exit(1);
        }
    }



}
