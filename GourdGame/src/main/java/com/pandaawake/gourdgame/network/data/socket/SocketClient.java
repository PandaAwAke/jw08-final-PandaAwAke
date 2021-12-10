package com.pandaawake.gourdgame.network.data.socket;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.Config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;


// From https://github.com/arukshani/JavaIOAndNIO
/**
 *
 * Test client for NIO server
 *
 */

public class SocketClient {

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
            
        }

        dataRead = new LinkedList<>();
    }

    public void run() {
        synchronized (this) {
            try {
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
            } catch (IOException e) {
                Log.app().fatal(this.getClass().getName() + ": IOException when running SocketClient!");
                e.printStackTrace();
            }
        }
    }

    // ------------------- Public functions -------------------
    public void writeData(byte[] data) throws IOException {
        synchronized (this) {
            ByteBuffer buffer = ByteBuffer.allocate(data.length);
            buffer.put(data);
            buffer.flip();
            channel.write(buffer);
            buffer.clear();
        }
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

    public void close() {
        synchronized (this) {
            try {
                channel.close();
            } catch (IOException e) {
                Log.app().fatal(this.getClass().getName() + ": IOException when closing SocketClient!");
                e.printStackTrace();
                
            }
        }
    }



}
