package com.pandaawake.gourdgame.network.data;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.player.action.Action;
import com.pandaawake.gourdgame.utils.UtilFunctions;

public class ServerDataProcessor extends DataProcessor {

    public ServerDataProcessor() {
        
    }

    @Override
    public Action ProcessData(byte[] data) {
        int number = UtilFunctions.getHeaderNumber(data, 0);

        byte[] info = new byte[1024];
        System.arraycopy(data, 4, info, 0, data.length - 4);

        switch (number) {
            // Connection Signals
            case ClientEnter:
                
                break;
            case ClientExit:

                break;
            // Game Action Signals
            case ClientServerPlayerAction:
                return Action.bytesToPlayerAction(info);
            default:
                Log.app().error(this.getClass().getName() + ": Received some illegal data?");
                break;
        }
        return null;
    }

}
