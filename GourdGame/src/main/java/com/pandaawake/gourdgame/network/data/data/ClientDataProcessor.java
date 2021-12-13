package com.pandaawake.gourdgame.network.data.data;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.network.data.action.Action;
import com.pandaawake.gourdgame.network.data.action.ConnectionAction;
import com.pandaawake.gourdgame.network.data.action.GameAction;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.utils.DataUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ClientDataProcessor extends DataProcessor {

    private final Scene scene;

    public ClientDataProcessor(Scene scene) {
        this.scene = scene;
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
                    // Game Signals
                    case GAME_START:
                        result.add(new GameAction.GameStart(senderClientId));
                        break;
                    case GAME_PAUSE:
                        result.add(new GameAction.GamePause(senderClientId));
                        break;
                    case GAME_RESUME:
                        result.add(new GameAction.GameResume(senderClientId));
                        break;
                    case GAME_END:
                        result.add(new GameAction.GameEnd(senderClientId));
                        break;

                    // Connection Signals
                    case SERVER_CLIENT_SUCCESSFULLY_ACCEPTED:
                        if (iStream.read(fourBytes) != 4) {
                            Log.app().error(this.getClass().getName() + "dataToActions: Illegal data format!");
                        }
                        int playerId = DataUtils.bytesToInt(fourBytes);
                        result.add(new ConnectionAction.ClientSuccessfullyAccepted(senderClientId, playerId));
                        break;
                    case SERVER_CLIENT_UNSUCCESSFULLY_ACCEPTED:
                        if (iStream.read(fourBytes) != 4) {
                            Log.app().error(this.getClass().getName() + "dataToActions: Illegal data format!");
                        }
                        int errorReasonBytesLen = DataUtils.bytesToInt(fourBytes);
                        byte[] errorReasonBytes = new byte[errorReasonBytesLen];
                        if (iStream.read(errorReasonBytes) != errorReasonBytesLen) {
                            Log.app().error(this.getClass().getName() + "dataToActions: Illegal data format!");
                        }
                        String errorReason = new String(errorReasonBytes);
                        result.add(new ConnectionAction.ClientUnsuccessfullyAccepted(senderClientId, errorReason));
                        break;
                    case SERVER_CLOSED:
                        result.add(new ConnectionAction.ServerClosed(senderClientId));
                        break;

                    // Player Action Signals
                    case SERVER_GAME_INITIALIZE:
                        result.add(GameAction.GameInitialize.parseBytes(senderClientId, iStream, scene));
                        break;
                    case SERVER_CLIENT_PLAYER_ACTION:
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
