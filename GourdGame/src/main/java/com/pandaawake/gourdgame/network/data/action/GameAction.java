package com.pandaawake.gourdgame.network.data.action;

import com.pandaawake.gourdgame.player.Player;
import com.pandaawake.gourdgame.utils.UtilFunctions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class GameAction extends Action {


    // Game Actions
    public static class GameStart extends GameAction {}

    public static class GamePause extends GameAction {}

    public static class GameResume extends GameAction {}

    public static class GameEnd extends GameAction {}

    public static class GameInitialize extends GameAction {
        public Collection<Player> players;
        public GameInitialize(Collection<Player> players) { this.players = players; }

        public byte[] toBytes() throws IOException {
            // [playerCount (4)] [player, player, ..., player]
            // [player] = [playerBytesCount(4)] [playerBytes (playerBytesCount)]
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();

            oStream.write(UtilFunctions.intToBytes(players.size()));
            int off = 4;
            for (Player player : players) {
                byte[] playerBytes = player.toBytes();
                int len = playerBytes.length;
                oStream.write(UtilFunctions.intToBytes(len), off, 4);
                off += 4;
                oStream.write(playerBytes, off, len);
                off += len;
            }

            return oStream.toByteArray();
        }

        public static GameInitialize parseBytes(byte[] bytes) {
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
            return new GameInitialize(players);
        }
    }



}
