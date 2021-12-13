package com.pandaawake.gourdgame.network.data.action;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.utils.DataUtils;
import com.pandaawake.gourdgame.utils.Direction;

import java.io.IOException;
import java.util.Date;

public abstract class PlayerAction extends Action {
    // Player action numbers
    public static final int PLAYER_NO_ACTION = 0;
    public static final int PLAYER_DO_MOVE = 1;
    public static final int PLAYER_SET_BOMB = 2;
    public static final int PLAYER_EXPLODE_BOMB = 3;

    // Extra info numbers
    public static final int EXTRA_INFO_NOTHING = 0;
    public static final int EXTRA_INFO_DIRECTION_LEFT = 1;
    public static final int EXTRA_INFO_DIRECTION_UP = 2;
    public static final int EXTRA_INFO_DIRECTION_RIGHT = 3;
    public static final int EXTRA_INFO_DIRECTION_DOWN = 4;

    public Date time = null;
    public int playerId;

    public PlayerAction(int senderClientId, int playerId) {
        super(senderClientId);
        this.playerId = playerId;
        time = new Date();
    }

    public byte[] toBytes() throws IOException {
        // [playerId] [this.toBytes()]
        // this.toBytes() = [type number (4)] [otherInfoNumbers (0 or 4)]
        return DataUtils.intToBytes(playerId);
    }

    public static PlayerAction parseBytes(int senderClientId, byte[] bytes) {
        int playerId = DataUtils.getHeaderNumber(bytes, 0);
        int off = 4;
        int playerActionNumber = DataUtils.getHeaderNumber(bytes, off);

        switch (playerActionNumber) {
            case PLAYER_NO_ACTION:
                return new NoAction(senderClientId, playerId);
            case PLAYER_DO_MOVE:
                int directionNumber = DataUtils.getHeaderNumber(bytes, off);
                Direction direction = null;

                switch (directionNumber) {
                    case EXTRA_INFO_DIRECTION_LEFT:
                        direction = Direction.left;
                        break;
                    case EXTRA_INFO_DIRECTION_UP:
                        direction = Direction.up;
                        break;
                    case EXTRA_INFO_DIRECTION_RIGHT:
                        direction = Direction.right;
                        break;
                    case EXTRA_INFO_DIRECTION_DOWN:
                        direction = Direction.down;
                        break;
                }
                return new DoMove(senderClientId, playerId, direction);

            case PLAYER_SET_BOMB:
                return new SetBomb(senderClientId, playerId);
                
            case PLAYER_EXPLODE_BOMB:
                return new ExplodeBomb(senderClientId, playerId);
        }
        return null;
    }

    public static PlayerAction parseString(String str, Date time) {
        String[] params = str.split(" ", 3);

        if (params.length < 2) {
            Log.app().error("Illegal params length!");
        }

        // Player id
        int playerId = Integer.parseInt(params[0]);

        // Action kind
        String actionKind = params[1];

        PlayerAction action = null;

        switch (actionKind) {
            case "NoAction":
                action = new PlayerAction.NoAction(playerId, playerId);
                break;
            case "DoMove":
                if (params.length != 3) {
                    Log.app().error("Illegal params length!");
                }
                String directionStr = params[2];
                Direction direction = null;
                switch (directionStr) {
                    case "left":
                        direction = Direction.left;
                        break;
                    case "up":
                        direction = Direction.up;
                        break;
                    case "right":
                        direction = Direction.right;
                        break;
                    case "down":
                        direction = Direction.down;
                        break;
                }
                action = new PlayerAction.DoMove(playerId, playerId, direction);
                break;
            case "ExplodeBomb":
                action = new PlayerAction.ExplodeBomb(playerId, playerId);
                break;
            case "SetBomb":
                action = new PlayerAction.SetBomb(playerId, playerId);
                break;
            default:
                Log.app().error("Illegal action kind!");
                return null;
        }

        action.playerId = playerId;
        action.time = time;

        return action;
    }






    // Player Actions
    public static class NoAction extends PlayerAction {
        public NoAction(int senderClientId, int playerId) {
            super(senderClientId, playerId);
        }

        @Override
        public String toString() {
            return "" + playerId + " NoAction";
        }

        @Override
        public byte[] toBytes() throws IOException {
            return DataUtils.concatBytes(super.toBytes(), DataUtils.intToBytes(PLAYER_NO_ACTION));
        }
    }

    public static class DoMove extends PlayerAction {
        public Direction direction;
        public DoMove(int senderClientId, int playerId, Direction direction) {
            super(senderClientId, playerId);
            this.direction = direction;
        }

        @Override
        public String toString() {
            return "" + playerId + " DoMove " + direction.toString();
        }

        @Override
        public byte[] toBytes() throws IOException {
            byte[] data1 = super.toBytes();
            byte[] data2 = DataUtils.intToBytes(PLAYER_DO_MOVE);
            byte[] data3 = null;
            switch (((DoMove) this).direction) {
                case left:
                    data3 = DataUtils.intToBytes(EXTRA_INFO_DIRECTION_LEFT);
                    break;
                case up:
                    data3 = DataUtils.intToBytes(EXTRA_INFO_DIRECTION_UP);
                    break;
                case right:
                    data3 = DataUtils.intToBytes(EXTRA_INFO_DIRECTION_RIGHT);
                    break;
                case down:
                    data3 = DataUtils.intToBytes(EXTRA_INFO_DIRECTION_DOWN);
                    break;
            }
            return DataUtils.concatBytes(data1, data2, data3);
        }
    }

    public static class SetBomb extends PlayerAction {
        public SetBomb(int senderClientId, int playerId) {
            super(senderClientId, playerId);
        }

        @Override
        public String toString() {
            return "" + playerId + " SetBomb";
        }

        @Override
        public byte[] toBytes() throws IOException {
            return DataUtils.concatBytes(super.toBytes(), DataUtils.intToBytes(PLAYER_SET_BOMB));
        }
    }

    public static class ExplodeBomb extends PlayerAction {
        public ExplodeBomb(int senderClientId, int playerId) {
            super(senderClientId, playerId);
        }

        @Override
        public String toString() {
            return "" + playerId + " ExplodeBomb";
        }

        @Override
        public byte[] toBytes() throws IOException {
            return DataUtils.concatBytes(super.toBytes(), DataUtils.intToBytes(PLAYER_EXPLODE_BOMB));
        }
    }
    
}
