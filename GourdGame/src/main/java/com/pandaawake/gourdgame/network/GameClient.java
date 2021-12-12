package com.pandaawake.gourdgame.network;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.main.ClientGameApp;
import com.pandaawake.gourdgame.network.data.action.Action;
import com.pandaawake.gourdgame.network.data.data.ClientDataProcessor;
import com.pandaawake.gourdgame.network.data.performer.ClientActionPerformer;
import com.pandaawake.gourdgame.network.data.socket.SocketClient;

import java.io.IOException;


public class GameClient {

    boolean running = true;

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }

    private final SocketClient socketClient;
    private ClientDataProcessor dataProcessor;
    private ClientActionPerformer actionPerformer;

    public GameClient(ClientGameApp app) {
        socketClient = new SocketClient();
        dataProcessor = new ClientDataProcessor(app.getScene());
        actionPerformer = new ClientActionPerformer(app);
        actionPerformer.setGameClient(this);
    }

    public SocketClient getSocketClient() {
        return socketClient;
    }


    public void sendAction(Action action) {
        synchronized (this) {
            try {
                byte[] data = dataProcessor.actionToData(action);
                socketClient.writeData(data);
            } catch (IOException e) {
                Log.app().error(getClass().getName() + ": IOException when sendAction!");
                e.printStackTrace();
            }
        }
    }




    void run() {
        synchronized (socketClient) {
            socketClient.run();
            while (socketClient.hasDataToHandle()) {
                byte[] data = socketClient.pollDataToHandle();
                Action action = dataProcessor.dataToAction(-1, data);
                actionPerformer.performAction(action);
            }
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
