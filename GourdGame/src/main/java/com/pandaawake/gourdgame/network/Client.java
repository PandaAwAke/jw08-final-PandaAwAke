package com.pandaawake.gourdgame.network;

import com.pandaawake.gourdgame.network.data.ClientDataProcessor;
import com.pandaawake.gourdgame.player.action.Action;
import com.pandaawake.gourdgame.player.action.ClientActionPerformer;


public class Client {

    boolean running = true;

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }

    SocketClient socketClient;
    ClientDataProcessor dataProcessor;
    ClientActionPerformer actionPerformer;

    public Client(ClientActionPerformer actionPerformer) {
        socketClient = new SocketClient();
        dataProcessor = new ClientDataProcessor();
        this.actionPerformer = actionPerformer;
    }

    public void run() {
        socketClient.run();
        while (socketClient.hasDataToHandle()) {
            byte[] data = socketClient.pollDataToHandle();
            Action action = dataProcessor.ProcessData(data);
            actionPerformer.performAction(action);
        }
    }


    
    public static class ClientRunnable implements Runnable {
        private Client client;

        public ClientRunnable(Client client) {
            this.client = client;
        }

        @Override
        public void run() {
            while (client.isRunning()) {
                client.run();
            }
            client.socketClient.close();
        }
    }

}
