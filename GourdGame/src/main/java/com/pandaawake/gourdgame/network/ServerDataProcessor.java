package com.pandaawake.gourdgame.network;

public class ServerDataProcessor extends DataProcessor {

    private GameServer server;

    public ServerDataProcessor(GameServer server) {
        this.server = server;
    }

    @Override
    public Action ProcessData(byte[] data) {
        byte[] numberBytes = new byte[4];
        System.arraycopy(data, 0, numberBytes, 0, 4);
        int number = bytesToInt(numberBytes);

        switch (number) {
            // Connection Signals
            case ClientEnter:

                break;
            case ClientExit:

                break;

            // Game Action Signals
            case ClientServerPlayerAction:
                new Actions.PlayerNoAction();
                break;
        }
        return null;
    }

}
