package com.pandaawake.gourdgame.network.data.data;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.network.data.action.Action;
import com.pandaawake.gourdgame.network.data.action.ConnectionAction;
import com.pandaawake.gourdgame.network.data.action.GameAction;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;
import com.pandaawake.gourdgame.utils.DataUtils;

import java.io.IOException;

public class ServerDataProcessor extends DataProcessor {

    public ServerDataProcessor() {
        
    }

    // ---------------------- Describes which data to handle ----------------------
    @Override
    public Action dataToAction(int senderClientId, byte[] data) {
        int number = DataUtils.getHeaderNumber(data, 0);

        byte[] info = new byte[4096];
        System.arraycopy(data, 4, info, 0, data.length - 4);

        switch (number) {
            // Connection Signals
            case CLIENT_ENTER:
                return new ConnectionAction.ClientEnter(senderClientId);
            case CLIENT_EXIT:
                return new ConnectionAction.ClientExit(senderClientId);
                
            // Player Action Signals
            case CLIENT_SERVER_PLAYER_ACTION:
                return PlayerAction.parseBytes(senderClientId, info);
            default:
                Log.app().error(this.getClass().getName() + ": Received some illegal data?");
                break;
        }
        return null;
    }


    // ---------------------- Describes which data to send ----------------------
    @Override
    protected byte[] actionToData(GameAction action) throws IOException {
        if (action instanceof GameAction.GameStart) {
            return DataUtils.intToBytes(GAME_START);
        } else if (action instanceof GameAction.GamePause) {
            return DataUtils.intToBytes(GAME_PAUSE);
        } else if (action instanceof GameAction.GameResume) {
            return DataUtils.intToBytes(GAME_RESUME);
        } else if (action instanceof GameAction.GameEnd) {
            return DataUtils.intToBytes(GAME_END);
        } else if (action instanceof GameAction.GameInitialize) {
            byte[] number = DataUtils.intToBytes(SERVER_GAME_INITIALIZE);
            byte[] actionBytes = ((GameAction.GameInitialize) action).toBytes();
            return DataUtils.concatBytes(number, actionBytes);
        }
        return null;
    }

    @Override
    protected byte[] actionToData(ConnectionAction action) throws IOException {
        if (action instanceof ConnectionAction.ClientSuccessfullyAccepted) {
            return DataUtils.intToBytes(SERVER_CLIENT_SUCCESSFULLY_ACCEPTED);
        } else if (action instanceof ConnectionAction.ClientUnsuccessfullyAccepted) {
            return DataUtils.intToBytes(SERVER_CLIENT_UNSUCCESSFULLY_ACCEPTED);
        } else if (action instanceof ConnectionAction.ServerClosed) {
            return DataUtils.intToBytes(SERVER_CLOSED);
        } else {
            Log.app().error(this.getClass().getName() + ": Null action or illegal action to send!");
        }
        return null;
    }

    @Override
    protected byte[] actionToData(PlayerAction action) throws IOException {
        byte[] number = DataUtils.intToBytes(SERVER_CLIENT_PLAYER_ACTION);
        byte[] actionBytes = action.toBytes();
        return DataUtils.concatBytes(number, actionBytes);
    }

}
