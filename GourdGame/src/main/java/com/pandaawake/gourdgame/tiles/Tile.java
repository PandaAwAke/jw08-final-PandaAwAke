package com.pandaawake.gourdgame.tiles;

import com.mandas.tiled2d.utils.pair.IntPair;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.utils.DataUtils;
import com.pandaawake.gourdgame.utils.ToBytes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Tile implements ToBytes {

    private Thing thing;
    private int xPos;
    private int yPos;

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public IntPair getIntPair() {
        return new IntPair(xPos, yPos);
    }

    public Tile() {
        this.thing = null;
        this.xPos = -1;
        this.yPos = -1;
    }

    public Tile(int xPos, int yPos) {
        this.thing = null;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
    }

    public Thing getThing() {
        return thing;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        oStream.write(DataUtils.intToBytes(xPos));
        oStream.write(DataUtils.intToBytes(yPos));
        return oStream.toByteArray();
    }

    public static Tile parseBytes(byte[] data, Scene scene) throws IOException {
        ByteArrayInputStream iStream = new ByteArrayInputStream(data);

        byte[] fourBytes = new byte[4];
        iStream.read(fourBytes); int xPos = DataUtils.bytesToInt(fourBytes);
        iStream.read(fourBytes); int yPos = DataUtils.bytesToInt(fourBytes);

        return scene.getGameMap().getTile(xPos, yPos);
    }
}
