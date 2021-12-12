package com.pandaawake.gourdgame.network.data.action;

import com.pandaawake.gourdgame.player.Player;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.utils.DataUtils;

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

        public static GameInitialize parseBytes(int senderClientId, byte[] bytes, Scene scene) throws IOException {
            Set<Player> players = new HashSet<>();
            int playerCount = DataUtils.getHeaderNumber(bytes, 0);
            int off = 4;
            for (int i = 0; i < playerCount; i++) {
                int playerInfoBytesCount = DataUtils.getHeaderNumber(bytes, off);
                off += 4;
                byte[] playerInfoBytes = new byte[4096];
                System.arraycopy(bytes, off, playerInfoBytes, 0, playerInfoBytesCount);
                off += playerInfoBytesCount;

                players.add(Player.parseBytes(playerInfoBytes, scene));
            }
            return new GameInitialize(senderClientId, players);
        }
    }



}
