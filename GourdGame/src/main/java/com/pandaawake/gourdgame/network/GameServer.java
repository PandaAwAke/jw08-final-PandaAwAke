package com.pandaawake.gourdgame.network;

import com.pandaawake.gourdgame.network.data.data.ServerDataProcessor;
import com.pandaawake.gourdgame.network.data.action.Action;
import com.pandaawake.gourdgame.network.data.performer.ServerActionPerformer;
import com.pandaawake.gourdgame.network.data.socket.SocketServer;


public class GameServer {
    
    private boolean running = true;

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }

    private SocketServer socketServer;
    private ServerDataProcessor dataProcessor;
    private ServerActionPerformer actionPerformer;

    public GameServer(ServerActionPerformer actionPerformer) {
        socketServer = new SocketServer();
        dataProcessor = new ServerDataProcessor();
        this.actionPerformer = actionPerformer;
        actionPerformer.setGameServer(this);
    }

    public SocketServer getSocketServer() {
        return socketServer;
    }

    public void run() {
        socketServer.run();
        while (socketServer.hasDataToHandle()) {
            byte[] data = socketServer.pollDataToHandle();
            Action action = dataProcessor.dataToAction(data);
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
