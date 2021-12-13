package com.pandaawake.gourdgame.network;

import com.mandas.tiled2d.core.Log;
import com.mandas.tiled2d.utils.Pair;
import com.pandaawake.gourdgame.main.ServerGameApp;
import com.pandaawake.gourdgame.network.data.action.Action;
import com.pandaawake.gourdgame.network.data.data.ServerDataProcessor;
import com.pandaawake.gourdgame.network.data.performer.ServerActionPerformer;
import com.pandaawake.gourdgame.network.data.socket.SocketServer;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;


public class GameServer {
    
    private boolean running = true;

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }

    private final SocketServer socketServer;
    private ServerDataProcessor dataProcessor;
    private ServerActionPerformer actionPerformer;

    public GameServer(ServerGameApp app) {
        socketServer = new SocketServer();
        dataProcessor = new ServerDataProcessor();
        actionPerformer = new ServerActionPerformer(app);
        actionPerformer.setGameServer(this);
    }

    public Set<Integer> getClientIds() {
        synchronized (this) {
            return socketServer.getClientIds();
        }
    }

    public void sendAction(Action action) {
        synchronized (this) {
            try {
                byte[] data = dataProcessor.actionToData(action);
                socketServer.addDataToWrite(data);
            } catch (IOException e) {
                Log.app().error(getClass().getName() + ": IOException when sendAction!");
                e.printStackTrace();
            }
        }
    }

    public void sendAction(Action action, Collection<Integer> ignoreList) {
        synchronized (this) {
            try {
                byte[] data = dataProcessor.actionToData(action);
                socketServer.addDataToWrite(data, ignoreList);
            } catch (IOException e) {
                Log.app().error(getClass().getName() + ": IOException when sendAction!");
                e.printStackTrace();
            }
        }
    }

    public void sendAction(Action action, int ignoreId) {
        synchronized (this) {
            try {
                byte[] data = dataProcessor.actionToData(action);
                socketServer.addDataToWrite(data, ignoreId);
            } catch (IOException e) {
                Log.app().error(getClass().getName() + ": IOException when sendAction!");
                e.printStackTrace();
            }
        }
    }

    public void close() {
        socketServer.close();
    }



    void run() {
        synchronized (this) {
            socketServer.run();
            while (socketServer.hasDataToHandle()) {
                Pair<Integer, byte[]> data = socketServer.pollDataToHandle();
                Action action = dataProcessor.dataToAction(data.first, data.second);
                actionPerformer.performAction(action);
            }
        }
    }



    public static class GameServerRunnable implements Runnable {
        private final GameServer gameServer;

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
