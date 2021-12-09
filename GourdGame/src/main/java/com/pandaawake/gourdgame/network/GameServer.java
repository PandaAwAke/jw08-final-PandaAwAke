package com.pandaawake.gourdgame.network;

import com.pandaawake.gourdgame.network.data.ServerDataProcessor;
import com.pandaawake.gourdgame.player.action.Action;
import com.pandaawake.gourdgame.player.action.ServerActionPerformer;


public class GameServer {
    
    private boolean running = true;

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }

    SocketServer socketServer;
    ServerDataProcessor dataProcessor;
    ServerActionPerformer actionPerformer;

    public GameServer(ServerActionPerformer actionPerformer) {
        socketServer = new SocketServer();
        dataProcessor = new ServerDataProcessor();
        this.actionPerformer = actionPerformer;
    }

    public void run() {
        socketServer.run();
        while (socketServer.hasDataToHandle()) {
            byte[] data = socketServer.pollDataToHandle();
            Action action = dataProcessor.ProcessData(data);
            actionPerformer.performAction(action);
        }
    }

    
    
    public static class GameServerRunnable implements Runnable {
        private GameServer gameServer;

        public GameServerRunnable(GameServer gameServer) {
            this.gameServer = gameServer;
        }

        @Override
        public void run() {
            while (gameServer.isRunning()) {
                gameServer.run();
            }
            gameServer.socketServer.close();
        }
    }

}
