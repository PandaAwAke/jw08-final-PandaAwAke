package com.pandaawake.gourdgame.network;

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

    public byte[] actionToData(Action action, boolean senderIsServer) {
        if (action instanceof Actions.GameStart) {
            return intToBytes(GameStart);
        } else if (action instanceof Actions.GamePause) {
            return intToBytes(GamePause);
        } else if (action instanceof Actions.GameResume) {
            return intToBytes(GameResume);
        } else if (action instanceof Actions.GameEnd) {
            return intToBytes(GameEnd);
        } else if (action instanceof Actions.GameInitialize) {
            byte[] number = intToBytes(ServerGameInitialize);
            // TODO

        } else {
            byte[] number;
            if (senderIsServer) {
                number = intToBytes(ServerClientPlayerAction);
            } else {
                number = intToBytes(ClientServerPlayerAction);
            }

            // TODO


            if (action instanceof Actions.PlayerNoAction) {

            } else if (action instanceof Actions.PlayerDoMove) {

            } else if (action instanceof Actions.PlayerSetBomb) {

            } else if (action instanceof Actions.PlayerExplodeBomb) {

            }
        }
        return null;
    }

    // -------------- Util functions --------------
    protected byte[] intToBytes(int num) {
        byte[] result = new byte[4];
        result[0] = (byte)((num >>> 24) & 0xff);
        result[1] = (byte)((num >>> 16) & 0xff);
        result[2] = (byte)((num >>> 8)  & 0xff);
        result[3] = (byte)((num >>> 0)  & 0xff);
        return result;
    }
    protected int bytesToInt(byte[] bytes) {
        int result = 0;
        if (bytes.length == 4) {
            int a = (bytes[0] & 0xff) << 24;
            int b = (bytes[1] & 0xff) << 16;
            int c = (bytes[2] & 0xff) << 8;
            int d = (bytes[3] & 0xff);
            result = a | b | c | d;
        }
        return result;
    }

}
