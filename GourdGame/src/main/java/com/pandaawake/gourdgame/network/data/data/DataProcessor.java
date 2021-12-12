package com.pandaawake.gourdgame.network.data.data;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.network.data.action.Action;
import com.pandaawake.gourdgame.network.data.action.ConnectionAction;
import com.pandaawake.gourdgame.network.data.action.GameAction;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;

import java.io.IOException;

public abstract class DataProcessor {

    // -------------- Game Network Numbers --------------
    // Game Signals
    public static final int GAME_START = 100;
    public static final int GAME_PAUSE = 101;
    public static final int GAME_RESUME = 102;
    public static final int GAME_END = 103;

    // Signals name starts with "Server" represents a transfer from SocketServer to SocketClient,
    // Signals name starts with "Client" represents a transfer from SocketClient to SocketServer.

    // Connection Signals
    public static final int CLIENT_ENTER = 200;
    public static final int CLIENT_EXIT = 201;
    public static final int SERVER_CLIENT_SUCCESSFULLY_ACCEPTED = 202;
    public static final int SERVER_CLIENT_UNSUCCESSFULLY_ACCEPTED = 203;
    public static final int SERVER_CLOSED = 204;

    // Game Action Signals
    public static final int SERVER_GAME_INITIALIZE = 300;
    public static final int SERVER_CLIENT_PLAYER_ACTION = 400;
    public static final int CLIENT_SERVER_PLAYER_ACTION = 401;

    // -------------- Functions --------------
    public abstract Action dataToAction(int senderClientId, byte[] data);

    protected abstract byte[] actionToData(GameAction action) throws IOException;
    protected abstract byte[] actionToData(ConnectionAction action) throws IOException;
    protected abstract byte[] actionToData(PlayerAction action) throws IOException;

    public byte[] actionToData(Action action) throws IOException {
        if (action instanceof GameAction) {
            return actionToData((GameAction) action);
        } else if (action instanceof ConnectionAction) {
            return actionToData((ConnectionAction) action);
        } else if (action instanceof PlayerAction) {
            return actionToData((PlayerAction) action);
        } else {
            Log.app().error(getClass().getName() + ": Null action or unsupported action!");
        }
        return null;
    }

}
