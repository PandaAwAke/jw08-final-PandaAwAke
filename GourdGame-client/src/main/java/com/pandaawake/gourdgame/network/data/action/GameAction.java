package com.pandaawake.gourdgame.network.data.action;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.utils.DataUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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

        public boolean humanWins = false;

        public GameEnd(int senderClientId, boolean humanWins) {
            super(senderClientId);
            this.humanWins = humanWins;
        }

    }

    public static class GameInitialize extends GameAction {
        public int spriteId;
        public String playerName;

        public GameInitialize(int senderClientId, int spriteId, String playerName) {
            super(senderClientId);
            this.spriteId = spriteId;
            this.playerName = playerName;
        }

        public byte[] toBytes() throws IOException {
            return DataUtils.concatBytes(DataUtils.intToBytes(spriteId), DataUtils.addLengthHeader(playerName.getBytes()));
        }

        public static GameInitialize parseBytes(int senderClientId, ByteArrayInputStream iStream) throws IOException {
            byte[] fourBytes = new byte[4];
            if (iStream.read(fourBytes) != 4) {
                Log.app().error("GameInitialize.parseBytes() : Illegal data format!");
            }
            int spriteId = DataUtils.bytesToInt(fourBytes);
            if (iStream.read(fourBytes) != 4) {
                Log.app().error("GameInitialize.parseBytes() : Illegal data format!");
            }
            int strBytesLen = DataUtils.bytesToInt(fourBytes);
            byte[] strBytes = new byte[strBytesLen];
            if (iStream.read(strBytes) != strBytesLen) {
                Log.app().error("GameInitialize.parseBytes() : Illegal data format!");
            }
            String playerName = new String(strBytes);
            return new GameInitialize(senderClientId, spriteId, playerName);
        }
    }

}
