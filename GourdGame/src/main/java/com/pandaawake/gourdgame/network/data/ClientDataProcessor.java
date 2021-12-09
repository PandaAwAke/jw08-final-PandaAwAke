package com.pandaawake.gourdgame.network.data;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.player.action.Action;
import com.pandaawake.gourdgame.player.action.Actions;
import com.pandaawake.gourdgame.utils.UtilFunctions;


public class ClientDataProcessor extends DataProcessor {

    public ClientDataProcessor() {
        
    }

    @Override
    public Action ProcessData(byte[] data) {

        int number = UtilFunctions.getHeaderNumber(data, 0);

        byte[] info = new byte[1024];
        System.arraycopy(data, 4, info, 0, data.length - 4);

        switch (number) {
            // Game Signals
            case GameStart:
                return new Actions.GameStart();
            case GamePause:
                return new Actions.GamePause();
            case GameResume:
                return new Actions.GameResume();
            case GameEnd:
                return new Actions.GameEnd();

            // Connection Signals
            case ServerClientSuccessfullyAccepted:

                break;
            case ServerClientUnsuccessfullyAccepted:
                String errorReason = new String(info);
                Log.app().fatal(this.getClass().getName() + ": Failed to connect to the GameSocketServer! " + errorReason);

                break;
            case ServerClosed:

                break;

            // Game Action Signals
            case ServerGameInitialize:
                return Action.bytesToGameInitialize(info);
            case ServerClientPlayerAction:
                return Action.bytesToPlayerAction(info);
            default:
                Log.app().error(this.getClass().getName() + ": Received some illegal data?");
                break;
        }
        return null;
    }

}
