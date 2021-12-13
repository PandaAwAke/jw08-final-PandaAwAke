package com.pandaawake.gourdgame.network.data.action;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.player.Player;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.utils.DataUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class GameAction extends Action {

    public GameAction(int senderClientId) {
        super(senderClientId);
    }


    // Game Actions
    public static class GameStart extends GameAction {

        public GameStart(int senderClientId) {
            super(senderClientId);
        }

    }

    public static class GamePause extends GameAction {

        public GamePause(int senderClientId) {
            super(senderClientId);
        }

    }

    public static class GameResume extends GameAction {

        public GameResume(int senderClientId) {
            super(senderClientId);
        }

    }

    public static class GameEnd extends GameAction {

        public GameEnd(int senderClientId) {
            super(senderClientId);
        }

    }

    public static class GameInitialize extends GameAction {
        public Collection<Player> players;
        public GameInitialize(int senderClientId, Collection<Player> players) {
            super(senderClientId);
            this.players = players;
        }

        public byte[] toBytes() throws IOException {
            // [playerCount (4)] [player, player, ..., player]
            // [player] = [playerBytesCount(4)] [playerBytes (playerBytesCount)]
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();

            oStream.write(DataUtils.intToBytes(players.size()));
            for (Player player : players) {
                oStream.write(DataUtils.addLengthHeader(player.toBytes()));
            }

            return oStream.toByteArray();
        }

        public static GameInitialize parseBytes(int senderClientId, ByteArrayInputStream iStream, Scene scene) throws IOException {
            Set<Player> players = new HashSet<>();
            byte[] fourBytes = new byte[4];
            if (iStream.read(fourBytes) != 4) {
                Log.app().error("GameInitialize.parseBytes() : Illegal data format!");
            }
            int playerCount = DataUtils.bytesToInt(fourBytes);
            for (int i = 0; i < playerCount; i++) {
                if (iStream.read(fourBytes) != 4) {
                    Log.app().error("GameInitialize.parseBytes() : Illegal data format!");
                }
                int playerInfoBytesCount = DataUtils.bytesToInt(fourBytes);
                byte[] playerInfoBytes = new byte[playerInfoBytesCount];
                if (iStream.read(playerInfoBytes) != playerInfoBytesCount) {
                    Log.app().error("GameInitialize.parseBytes() : Illegal data format!");
                }

                players.add(Player.parseBytes(playerInfoBytes, scene));
            }
            return new GameInitialize(senderClientId, players);
        }
    }



}
