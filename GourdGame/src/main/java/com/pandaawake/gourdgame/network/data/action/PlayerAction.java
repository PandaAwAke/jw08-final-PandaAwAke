package com.pandaawake.gourdgame.network.data.action;

import com.mandas.tiled2d.Config;
import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.utils.Direction;
import com.pandaawake.gourdgame.utils.UtilFunctions;

import java.io.ByteArrayOutputStream;
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

    public int playerId = 0;
    public Date time = null;

    public PlayerAction(int playerId) {
        this.playerId = playerId;
        time = new Date();
    }

    public String toString() {
        String result = "";
        result += Config.DateFormat.format(time);
        result += " " + playerId;
        return result;
    }

    public byte[] toBytes() {
        // [playerId] [type number (4)] [otherInfoNumbers (4)]
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        int off = 0;
        oStream.write(UtilFunctions.intToBytes(playerId), off, 4);
        off += 4;
        if (this instanceof NoAction) {
            oStream.write(UtilFunctions.intToBytes(PLAYER_NO_ACTION), off, 4);
            off += 4;
        } else if (this instanceof DoMove) {
            oStream.write(UtilFunctions.intToBytes(PLAYER_DO_MOVE), off, 4);
            off += 4;
            switch (((DoMove) this).direction) {
                case left:
                    oStream.write(UtilFunctions.intToBytes(EXTRA_INFO_DIRECTION_LEFT), off, 4);
                    off += 4;
                    break;
                case up:
                    oStream.write(UtilFunctions.intToBytes(EXTRA_INFO_DIRECTION_UP), off, 4);
                    off += 4;
                    break;
                case right:
                    oStream.write(UtilFunctions.intToBytes(EXTRA_INFO_DIRECTION_RIGHT), off, 4);
                    off += 4;
                    break;
                case down:
                    oStream.write(UtilFunctions.intToBytes(EXTRA_INFO_DIRECTION_DOWN), off, 4);
                    off += 4;
                    break;
            }
        } else if (this instanceof SetBomb) {
            oStream.write(UtilFunctions.intToBytes(PLAYER_SET_BOMB), off, 4);
            off += 4;
        } else if (this instanceof ExplodeBomb) {
            oStream.write(UtilFunctions.intToBytes(PLAYER_EXPLODE_BOMB), off, 4);
            off += 4;
        } else {
            return null;
        }

        return oStream.toByteArray();
    }

    public static PlayerAction parseString(String str, Date time) {
        String[] params = str.split(" ", 3);

        assert params.length >= 2;

        // Player id
        int playerId = Integer.parseInt(params[0]);

        // Action kind
        String actionKind = params[1];

        PlayerAction action = null;

        switch (actionKind) {
            case "NoAction":
                action = new PlayerAction.NoAction(playerId);
                break;
            case "DoMove":
                assert params.length == 3;
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
                action = new PlayerAction.DoMove(playerId, direction);
                break;
            case "ExplodeBomb":
                action = new PlayerAction.ExplodeBomb(playerId);
                break;
            case "SetBomb":
                action = new PlayerAction.SetBomb(playerId);
                break;
            default:
                Log.app().error("Illegal action kind!");
                return null;
        }

        action.playerId = playerId;
        action.time = time;

        return action;
    }

    public static PlayerAction parseBytes(byte[] bytes) {
        int playerActionNumber = UtilFunctions.getHeaderNumber(bytes, 0);
        int off = 4;
        int playerId = UtilFunctions.getHeaderNumber(bytes, off);

        off += 4;
        switch (playerActionNumber) {
            case PLAYER_NO_ACTION:
                return new NoAction(playerId);
            case PLAYER_DO_MOVE:
                int directionNumber = UtilFunctions.getHeaderNumber(bytes, off);
                Direction direction = null;
                off += 4;
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
                return new DoMove(playerId, direction);
            case PLAYER_SET_BOMB:
                return new SetBomb(playerId);
            case PLAYER_EXPLODE_BOMB:
                return new ExplodeBomb(playerId);
        }
        return null;
    }




    // Player Actions
    public static class NoAction extends PlayerAction {
        public NoAction(int playerId) {
            super(playerId);
        }

        @Override
        public String toString() {
            return super.toString() + " NoAction";
        }
    }

    public static class DoMove extends PlayerAction {
        public Direction direction;
        public DoMove(int playerId, Direction direction) {
            super(playerId);
            this.direction = direction;
        }

        @Override
        public String toString() {
            return super.toString() + " DoMove " + direction.toString();
        }
    }

    public static class SetBomb extends PlayerAction {
        public SetBomb(int playerId) {
            super(playerId);
        }

        @Override
        public String toString() {
            return super.toString() + " SetBomb";
        }
    }

    public static class ExplodeBomb extends PlayerAction {
        public ExplodeBomb(int playerId) {
            super(playerId);
        }

        @Override
        public String toString() {
            return super.toString() + " ExplodeBomb";
        }
    }
    
}
