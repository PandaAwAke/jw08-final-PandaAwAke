package com.pandaawake.gourdgame.network.data.data;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.network.data.action.*;
import com.pandaawake.gourdgame.utils.DataUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerDataProcessor extends DataProcessor {

    public ServerDataProcessor() {
        
    }

    // ---------------------- Describes which data to handle ----------------------
    @Override
    public List<Action> dataToActions(int senderClientId, byte[] data) {
        List<Action> result = new ArrayList<>();
        ByteArrayInputStream iStream = new ByteArrayInputStream(data);

        try {
            while (true) {
                byte[] fourBytes = new byte[4];
                int numRead = iStream.read(fourBytes);
                if (numRead == -1) {
                    break;
                }
                if (numRead != 4) {
                    Log.app().error(this.getClass().getName() + "dataToActions: Illegal data format!");
                }
                int number = DataUtils.bytesToInt(fourBytes);
                switch (number) {
                    // Connection Signals
                    case CLIENT_ENTER:
                        result.add(new ConnectionAction.ClientEnter(senderClientId));
                        break;
                    case CLIENT_EXIT:
                        result.add(new ConnectionAction.ClientExit(senderClientId));
                        break;

                    // Player Action Signals
                    case CLIENT_SERVER_PLAYER_ACTION:
                        result.add(PlayerAction.parseBytes(senderClientId, iStream));
                        break;
                    default:
                        Log.app().error(this.getClass().getName() + ": Received some illegal data?");
                        break;
                }
            }
        } catch (IOException e) {
            Log.app().error(this.getClass().getName() + ": IOException when dataToActions!");
            e.printStackTrace();
        }
        return result;
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
            return DataUtils.concatBytes(
                    DataUtils.intToBytes(GAME_END),
                    DataUtils.intToBytes(((GameAction.GameEnd) action).humanWins ? 1 : 0)
            );
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
            return DataUtils.concatBytes(
                    DataUtils.intToBytes(SERVER_CLIENT_SUCCESSFULLY_ACCEPTED),
                    DataUtils.intToBytes(((ConnectionAction.ClientSuccessfullyAccepted) action).playerId)
            );
        } else if (action instanceof ConnectionAction.ClientUnsuccessfullyAccepted) {
            byte[] errorReasonBytes = ((ConnectionAction.ClientUnsuccessfullyAccepted) action).errorReason.getBytes();
            return DataUtils.concatBytes(
                    DataUtils.intToBytes(SERVER_CLIENT_UNSUCCESSFULLY_ACCEPTED),
                    DataUtils.intToBytes(errorReasonBytes.length),
                    errorReasonBytes
            );
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

    @Override
    protected byte[] actionToData(SceneAction action) throws IOException {
        byte[] number = DataUtils.intToBytes(SERVER_SCENE_ACTION);
        byte[] actionBytes = action.toBytes();
        return DataUtils.concatBytes(number, actionBytes);
    }

}
