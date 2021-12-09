package com.pandaawake.gourdgame.network;

import com.pandaawake.gourdgame.utils.UtilFunctions;

public class ServerDataProcessor extends DataProcessor {

    private GameServer server;

    public ServerDataProcessor(GameServer server) {
        this.server = server;
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
        }
        return null;
    }

}
