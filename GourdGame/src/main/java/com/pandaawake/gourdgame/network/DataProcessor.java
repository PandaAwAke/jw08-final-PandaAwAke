package com.pandaawake.gourdgame.network;

import java.io.IOException;

import com.pandaawake.gourdgame.utils.UtilFunctions;

public abstract class DataProcessor {

    // -------------- Game Network Numbers --------------
    // Game Signals
    public static final int GameStart = 100;
    public static final int GamePause = 101;
    public static final int GameResume = 102;
    public static final int GameEnd = 103;

    // Signals name starts with "Server" represents a transfer from GameServer to Client,
    // Signals name starts with "Client" represents a transfer from Client to GameServer.

    // Connection Signals
    public static final int ClientEnter = 200;
    public static final int ClientExit = 201;
    public static final int ServerClientSuccessfullyAccepted = 202;
    public static final int ServerClientUnsuccessfullyAccepted = 203;

    // Game Action Signals
    public static final int ServerGameInitialize = 300;
    public static final int ServerClientPlayerAction = 400;
    public static final int ClientServerPlayerAction = 401;

    // -------------- Functions --------------
    public abstract Action ProcessData(byte[] data);

    public byte[] actionToData(Action action, boolean senderIsServer) throws IOException {
        if (action instanceof Actions.GameStart) {
            return UtilFunctions.intToBytes(GameStart);
        } else if (action instanceof Actions.GamePause) {
            return UtilFunctions.intToBytes(GamePause);
        } else if (action instanceof Actions.GameResume) {
            return UtilFunctions.intToBytes(GameResume);
        } else if (action instanceof Actions.GameEnd) {
            return UtilFunctions.intToBytes(GameEnd);
        } else if (action instanceof Actions.GameInitialize) {
            byte[] number = UtilFunctions.intToBytes(ServerGameInitialize);
            byte[] actionBytes = Action.gameInitializeToBytes((Actions.GameInitialize) action);
            return UtilFunctions.concatBytes(number, actionBytes);
        } else if (action instanceof PlayerAction) {
            byte[] number;
            if (senderIsServer) {
                number = UtilFunctions.intToBytes(ServerClientPlayerAction);
            } else {
                number = UtilFunctions.intToBytes(ClientServerPlayerAction);
            }
            byte[] actionBytes = Action.playerActionToBytes((PlayerAction) action);
            return UtilFunctions.concatBytes(number, actionBytes);
        }
        return null;
    }

    

}
