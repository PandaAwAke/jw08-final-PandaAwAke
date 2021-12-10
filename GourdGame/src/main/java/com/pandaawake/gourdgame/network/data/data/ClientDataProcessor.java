package com.pandaawake.gourdgame.network.data.data;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.network.data.action.Action;
import com.pandaawake.gourdgame.network.data.action.ConnectionAction;
import com.pandaawake.gourdgame.network.data.action.GameAction;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;
import com.pandaawake.gourdgame.utils.UtilFunctions;

import java.io.IOException;


public class ClientDataProcessor extends DataProcessor {

    public ClientDataProcessor() {
        
    }

    // ---------------------- Describes which data to handle ----------------------
    @Override
    public Action dataToAction(byte[] data) {
        int number = UtilFunctions.getHeaderNumber(data, 0);

        byte[] info = new byte[1024];
        System.arraycopy(data, 4, info, 0, data.length - 4);

        switch (number) {
            // Game Signals
            case GAME_START:
                return new GameAction.GameStart();
            case GAME_PAUSE:
                return new GameAction.GamePause();
            case GAME_RESUME:
                return new GameAction.GameResume();
            case GAME_END:
                return new GameAction.GameEnd();

            // Connection Signals
            case SERVER_CLIENT_SUCCESSFULLY_ACCEPTED:
                return new ConnectionAction.ClientSuccessfullyAccepted();
            case SERVER_CLIENT_UNSUCCESSFULLY_ACCEPTED:
                String errorReason = new String(info);
                return new ConnectionAction.ClientUnsuccessfullyAccepted(errorReason);
            case SERVER_CLOSED:
                return new ConnectionAction.ServerClosed();

            // Player Action Signals
            case SERVER_GAME_INITIALIZE:
                return GameAction.GameInitialize.parseBytes(info);
            case SERVER_CLIENT_PLAYER_ACTION:
                return PlayerAction.parseBytes(info);
            default:
                Log.app().error(this.getClass().getName() + ": Received some illegal data?");
                break;
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
        return null;
    }

    @Override
    protected byte[] actionToData(PlayerAction action) throws IOException {
        byte[] number = UtilFunctions.intToBytes(CLIENT_SERVER_PLAYER_ACTION);
        byte[] actionBytes = action.toBytes();
        return UtilFunctions.concatBytes(number, actionBytes);
    }

}
