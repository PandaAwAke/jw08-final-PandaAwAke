package com.pandaawake.gourdgame.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.pandaawake.gourdgame.player.Player;
import com.pandaawake.gourdgame.utils.Direction;
import com.pandaawake.gourdgame.utils.UtilFunctions;

public abstract class Action {

    // Player action numbers
    public static final int PlayerNoAction = 0;
    public static final int PlayerDoMove = 1;
    public static final int PlayerSetBomb = 2;
    public static final int PlayerExplodeBomb = 3;

    // Other info numbers
    public static final int OtherInfoNothing = 0;
    public static final int OtherInfoDirectionLeft = 1;
    public static final int OtherInfoDirectionUp = 2;
    public static final int OtherInfoDirectionRight = 3;
    public static final int OtherInfoDirectionDown = 4;




    public static byte[] gameInitializeToBytes(Actions.GameInitialize action) throws IOException {
        // [playerCount (4)] [player, player, ..., player]
        // [player] = [playerBytesCount(4)] [playerBytes (playerBytesCount)]
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        
        oStream.write(UtilFunctions.intToBytes(action.players.size()));
        int off = 4;
        for (Player player : action.players) {
            byte[] playerBytes = player.toBytes();
            int len = playerBytes.length;
            oStream.write(UtilFunctions.intToBytes(len), off, 4);
            off += 4;
            oStream.write(playerBytes, off, len);
            off += len;
        }

        return oStream.toByteArray();
    }

    public static Actions.GameInitialize bytesToGameInitialize(byte[] bytes) {
        Set<Player> players = new HashSet<>();
        int playerCount = UtilFunctions.getHeaderNumber(bytes, 0);
        int off = 4;
        for (int i = 0; i < playerCount; i++) {
            int playerInfoBytesCount = UtilFunctions.getHeaderNumber(bytes, off);
            off += 4;
            byte[] playerInfoBytes = new byte[1024];
            System.arraycopy(bytes, off, playerInfoBytes, 0, playerInfoBytesCount);
            off += playerInfoBytesCount;

            players.add(Player.parseBytes(playerInfoBytes));
        }
        return new Actions.GameInitialize(players);
    }


    public static byte[] playerActionToBytes(PlayerAction action) throws IOException {
        // [playerId] [type number (4)] [otherInfoNumbers (4)]
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        int off = 0;
        oStream.write(UtilFunctions.intToBytes(action.playerId), off, 4);
        off += 4;
        if (action instanceof Actions.PlayerNoAction) {
            oStream.write(UtilFunctions.intToBytes(PlayerNoAction), off, 4);
            off += 4;
        } else if (action instanceof Actions.PlayerDoMove) {
            oStream.write(UtilFunctions.intToBytes(PlayerDoMove), off, 4);
            off += 4;
            switch (((Actions.PlayerDoMove) action).direction) {
                case left:
                    oStream.write(UtilFunctions.intToBytes(OtherInfoDirectionLeft), off, 4);
                    off += 4;
                    break;
                case up:
                    oStream.write(UtilFunctions.intToBytes(OtherInfoDirectionUp), off, 4);
                    off += 4;
                    break;
                case right:
                    oStream.write(UtilFunctions.intToBytes(OtherInfoDirectionRight), off, 4);
                    off += 4;
                    break;
                case down:
                    oStream.write(UtilFunctions.intToBytes(OtherInfoDirectionDown), off, 4);
                    off += 4;
                    break;
            }
        } else if (action instanceof Actions.PlayerSetBomb) {
            oStream.write(UtilFunctions.intToBytes(PlayerSetBomb), off, 4);
            off += 4;
        } else if (action instanceof Actions.PlayerExplodeBomb) {
            oStream.write(UtilFunctions.intToBytes(PlayerExplodeBomb), off, 4);
            off += 4;
        } else {
            return null;
        }

        return oStream.toByteArray();
    }

    public static Action bytesToPlayerAction(byte[] bytes) {
        int playerActionNumber = UtilFunctions.getHeaderNumber(bytes, 0);
        int off = 4;
        int playerId = UtilFunctions.getHeaderNumber(bytes, off);
        off += 4;
        switch (playerActionNumber) {
            case PlayerNoAction:
                return new Actions.PlayerNoAction(playerId);
            case PlayerDoMove:
                int directionNumber = UtilFunctions.getHeaderNumber(bytes, off);
                Direction direction = null;
                off += 4;
                switch (directionNumber) {
                    case OtherInfoDirectionLeft:
                        direction = Direction.left;
                        break;
                    case OtherInfoDirectionUp:
                        direction = Direction.up;
                        break;
                    case OtherInfoDirectionRight:
                        direction = Direction.right;
                        break;
                    case OtherInfoDirectionDown:
                        direction = Direction.down;
                        break;
                }
                return new Actions.PlayerDoMove(playerId, direction);
            case PlayerSetBomb:
                return new Actions.PlayerSetBomb(playerId);
            case PlayerExplodeBomb:
                return new Actions.PlayerExplodeBomb(playerId);
        }
        return null;
    }

}
