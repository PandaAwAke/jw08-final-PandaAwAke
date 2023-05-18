package com.pandaawake.gourdgame.network;

import com.mandas.tiled2d.core.Log;
import com.mandas.tiled2d.utils.pair.Pair;
import com.pandaawake.gourdgame.main.ServerGameApp;
import com.pandaawake.gourdgame.network.data.action.Action;
import com.pandaawake.gourdgame.network.data.data.ServerDataProcessor;
import com.pandaawake.gourdgame.network.data.performer.ServerActionPerformer;
import com.pandaawake.gourdgame.network.data.socket.SocketServer;

import java.io.IOException;
import java.util.List;


public class GameServer {
    
    private boolean running = true;

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }

    private final SocketServer socketServer;
    private final ServerDataProcessor dataProcessor;
    private final ServerActionPerformer actionPerformer;

    public ServerActionPerformer getActionPerformer() {
        return actionPerformer;
    }

    public GameServer(ServerGameApp app) {
        socketServer = new SocketServer();

        dataProcessor = ServerDataProcessor.getInstance();

        ServerActionPerformer.initApp(app);
        actionPerformer = ServerActionPerformer.getInstance();

        actionPerformer.setGameServer(this);
    }

    public void sendAction(Action action) {
        try {
            byte[] data = dataProcessor.actionToData(action);
            socketServer.addDataToWrite(data);
        } catch (IOException e) {
            Log.app().error(getClass().getName() + ": IOException when sendAction!");
            e.printStackTrace();
        }
    }

    // public void sendAction(Action action, Collection<Integer> ignoreList) {
    //     synchronized (this) {
    //         try {
    //             byte[] data = dataProcessor.actionToData(action);
    //             socketServer.addDataToWrite(data, ignoreList);
    //         } catch (IOException e) {
    //             Log.app().error(getClass().getName() + ": IOException when sendAction!");
    //             e.printStackTrace();
    //         }
    //     }
    // }

    public void sendAction(Action action, int targetId) {
        try {
            byte[] data = dataProcessor.actionToData(action);
            socketServer.addDataToWrite(data, targetId);
        } catch (IOException e) {
            Log.app().error(getClass().getName() + ": IOException when sendAction!");
            e.printStackTrace();
        }
    }

    public void close() {
        socketServer.close();
    }



    void run() {
        socketServer.run();
        while (socketServer.hasDataToHandle()) {
            Pair<Integer, byte[]> data = socketServer.pollDataToHandle();
            List<Action> actions = dataProcessor.dataToActions(data.first, data.second);
            for (Action action : actions) {
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
