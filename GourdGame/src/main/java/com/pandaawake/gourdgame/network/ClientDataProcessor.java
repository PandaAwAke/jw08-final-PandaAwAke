package com.pandaawake.gourdgame.network;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.player.Player;

import java.util.HashSet;
import java.util.Set;

public class ClientDataProcessor extends DataProcessor {

    //private Client client;

    public ClientDataProcessor() {
        //this.client = client;
    }

    @Override
    public Action ProcessData(byte[] data) {
        byte[] numberBytes = new byte[4];
        byte[] info = new byte[1024];
        System.arraycopy(data, 0, numberBytes, 0, 4);
        System.arraycopy(data, 4, info, 0, data.length - 4);
        int number = bytesToInt(numberBytes);

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
                Log.app().error("Failed to connect to the GameServer! " + errorReason);

                break;

            // Game Action Signals
            case ServerGameInitialize:
                Set<Player> players = new HashSet<>();
                byte[] playerCountBytes = new byte[4];
                System.arraycopy(info, 0, playerCountBytes, 0, 4);
                int playerCount = bytesToInt(playerCountBytes);
                int infoBufferIndex = 4;
                for (int i = 0; i < playerCount; i++) {
                    byte[] playerInfoBytesCountBytes = new byte[4];
                    System.arraycopy(info, infoBufferIndex, playerInfoBytesCountBytes, 0, 4);
                    infoBufferIndex += 4;
                    int playerInfoBytesCount = bytesToInt(playerInfoBytesCountBytes);

                    byte[] playerInfoBytes = new byte[1024];
                    System.arraycopy(info, infoBufferIndex, playerInfoBytes, 0, playerInfoBytesCount);
                    infoBufferIndex += playerInfoBytesCount;

                    // TODO
                    // Player.parseBytes(playerInfoBytes);
                }
                return new Actions.GameInitialize(players);
            case ServerClientPlayerAction:
                return new Actions.PlayerNoAction();
        }
        return null;
    }

}
