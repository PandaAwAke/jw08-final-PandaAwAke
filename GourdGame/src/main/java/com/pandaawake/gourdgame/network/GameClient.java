package com.pandaawake.gourdgame.network;

import com.pandaawake.gourdgame.network.data.data.ClientDataProcessor;
import com.pandaawake.gourdgame.network.data.action.Action;
import com.pandaawake.gourdgame.network.data.performer.ClientActionPerformer;
import com.pandaawake.gourdgame.network.data.socket.SocketClient;


public class GameClient {

    boolean running = true;

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }

    private SocketClient socketClient;
    private ClientDataProcessor dataProcessor;
    private ClientActionPerformer actionPerformer;

    public GameClient(ClientActionPerformer actionPerformer) {
        socketClient = new SocketClient();
        dataProcessor = new ClientDataProcessor();
        this.actionPerformer = actionPerformer;
        actionPerformer.setGameClient(this);
    }

    public SocketClient getSocketClient() {
        return socketClient;
    }

    public void run() {
        socketClient.run();
        while (socketClient.hasDataToHandle()) {
            byte[] data = socketClient.pollDataToHandle();
            Action action = dataProcessor.dataToAction(data);
            actionPerformer.performAction(action);
        }
    }


    
    public static class GameClientRunnable implements Runnable {
        private GameClient gameClient;

        public GameClientRunnable(GameClient gameClient) {
            this.gameClient = gameClient;
        }

        @Override
        public void run() {
            while (gameClient.isRunning()) {
                gameClient.run();
            }
            gameClient.socketClient.close();
        }
    }

}
