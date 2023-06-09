package com.pandaawake.gourdgame.network;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.main.ClientGameApp;
import com.pandaawake.gourdgame.network.data.action.Action;
import com.pandaawake.gourdgame.network.data.data.ClientDataProcessor;
import com.pandaawake.gourdgame.network.data.performer.ClientActionPerformer;
import com.pandaawake.gourdgame.network.data.socket.SocketClient;

import java.io.IOException;
import java.util.List;


public class GameClient {

    boolean running = true;

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }

    private final SocketClient socketClient;
    private final ClientDataProcessor dataProcessor;
    private final ClientActionPerformer actionPerformer;


    public GameClient(ClientGameApp app) {
        socketClient = new SocketClient();

        ClientDataProcessor.initScene(app.getScene());
        dataProcessor = ClientDataProcessor.getInstance();

        ClientActionPerformer.initApp(app);
        actionPerformer = ClientActionPerformer.getInstance();

        actionPerformer.setGameClient(this);
    }


    public void sendAction(Action action) {
        try {
            byte[] data = dataProcessor.actionToData(action);
            socketClient.writeData(data);
        } catch (IOException e) {
            Log.app().error(getClass().getName() + ": IOException when sendAction!");
            e.printStackTrace();
        }
    }






    void run() {
        socketClient.run();
        while (socketClient.hasDataToHandle()) {
            byte[] data = socketClient.pollDataToHandle();
            List<Action> actions = dataProcessor.dataToActions(-1, data);
            for (Action action : actions) {
                actionPerformer.performAction(action);
            }
        }
        actionPerformer.handleRemainActions();
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
