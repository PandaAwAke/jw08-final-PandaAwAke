package com.pandaawake.gourdgame.network.data.data;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.network.data.action.Action;
import com.pandaawake.gourdgame.network.data.action.ConnectionAction;
import com.pandaawake.gourdgame.network.data.action.GameAction;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.utils.DataUtils;

import java.io.IOException;


public class ClientDataProcessor extends DataProcessor {

    private final Scene scene;

    public ClientDataProcessor(Scene scene) {
        this.scene = scene;
    }

    // ---------------------- Describes which data to handle ----------------------
    @Override
    public Action dataToAction(int senderClientId, byte[] data) {
        int number = DataUtils.getHeaderNumber(data, 0);

        byte[] info = new byte[4096];
        System.arraycopy(data, 4, info, 0, data.length - 4);

        try {
            switch (number) {
                // Game Signals
                case GAME_START:
                    return new GameAction.GameStart(senderClientId);
                case GAME_PAUSE:
                    return new GameAction.GamePause(senderClientId);
                case GAME_RESUME:
                    return new GameAction.GameResume(senderClientId);
                case GAME_END:
                    return new GameAction.GameEnd(senderClientId);

                // Connection Signals
                case SERVER_CLIENT_SUCCESSFULLY_ACCEPTED:
                    int playerId = DataUtils.getHeaderNumber(info, 0);
                    return new ConnectionAction.ClientSuccessfullyAccepted(senderClientId, playerId);
                case SERVER_CLIENT_UNSUCCESSFULLY_ACCEPTED:
                    String errorReason = new String(info);
                    return new ConnectionAction.ClientUnsuccessfullyAccepted(senderClientId, errorReason);
                case SERVER_CLOSED:
                    return new ConnectionAction.ServerClosed(senderClientId);

                // Player Action Signals
                case SERVER_GAME_INITIALIZE:
                    return GameAction.GameInitialize.parseBytes(senderClientId, info, scene);
                case SERVER_CLIENT_PLAYER_ACTION:
                    return PlayerAction.parseBytes(senderClientId, info);
                default:
                    Log.app().error(this.getClass().getName() + ": Received some illegal data?");
                    break;
            }
        } catch (IOException e) {
            Log.app().error(this.getClass().getName() + ": IOException when dataToAction!");
            e.printStackTrace();
        }

        return null;
    }


    // ---------------------- Describes which data to send ----------------------
    @Override
    protected byte[] actionToData(GameAction action) throws IOException {
        return null;
    }

    @Override
    protected byte[] actionToData(ConnectionAction action) throws IOException {
        if (action instanceof ConnectionAction.ClientEnter) {
            return DataUtils.intToBytes(CLIENT_ENTER);
        } else if (action instanceof ConnectionAction.ClientExit) {
            return DataUtils.intToBytes(CLIENT_EXIT);
        } else {
            Log.app().error(this.getClass().getName() + ": Null action or illegal action to send!");
        }
        return null;
    }

    @Override
    protected byte[] actionToData(PlayerAction action) throws IOException {
        byte[] number = DataUtils.intToBytes(CLIENT_SERVER_PLAYER_ACTION);
        byte[] actionBytes = action.toBytes();
        return DataUtils.concatBytes(number, actionBytes);
    }

}
