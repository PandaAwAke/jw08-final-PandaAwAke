package com.pandaawake.gourdgame.tiles;

import com.mandas.tiled2d.core.Log;
import com.mandas.tiled2d.renderer.Texture;
import com.mandas.tiled2d.renderer.TileTextures;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.utils.DataUtils;
import com.pandaawake.gourdgame.utils.ToBytes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Thing implements ToBytes {

    public static final int FLOOR = 1;
    public static final int TREE = 2;
    public static final int WALL = 3;
    public static final int TWICE_BREAKABLE_WALL = 4;


    protected int id;
    // Do not use Set, because array can maintain a sequence
    protected boolean blocking;
    protected ArrayList<Tile> tiles;
    protected TileTextures tileTextures;

    public Thing(int id, boolean blocking) {
        this.id = id;
        this.blocking = blocking;
        tiles = new ArrayList<>();
        tileTextures = new TileTextures();
    }

    public void addTile(Tile tile) {
        if (!tiles.contains(tile)) {
            tiles.add(tile);
        }
        tile.setThing(this);
    }

    public int getId() {
        return id;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public boolean isBlocking() {
        return this.blocking;
    }


    public ArrayList<Texture> getTextures() {
        if (tileTextures.getTextures() != null && tileTextures.getTextures().size() != tiles.size()) {
            throw new IllegalStateException("Textures' size does not equal to the tiles' size!");
        }
        return tileTextures.getTextures();
    }

    /**
     * This function decides whether this thing is destroyed by the bomb
     * @return true if this thing is destroyed by the bomb
     */
    public boolean OnExplode() {
        return false;
    }

    public void OnUpdate(float timestep) {
        
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        if (this instanceof Floor) {
            oStream.write(DataUtils.intToBytes(FLOOR));
        } else if (this instanceof Tree) {
            oStream.write(DataUtils.intToBytes(TREE));
        } else if (this instanceof Wall) {
            oStream.write(DataUtils.intToBytes(WALL));
        } else {
            oStream.write(DataUtils.intToBytes(TWICE_BREAKABLE_WALL));
        }
        oStream.write(DataUtils.intToBytes(id));
        oStream.write(DataUtils.intToBytes((blocking ? 1 : 0)));
        oStream.write(DataUtils.collectionToBytes(tiles));
        return oStream.toByteArray();
    }

    public static Thing parseBytes(byte[] data, Scene scene) throws IOException {
        ByteArrayInputStream iStream = new ByteArrayInputStream(data);
        byte[] fourBytes = new byte[4];
        iStream.read(fourBytes); int type = DataUtils.bytesToInt(fourBytes);
        Thing thing = null;
        switch (type) {
            case FLOOR:
                thing = new Floor(-1);
                break;
            case TREE:
                thing = new Tree(-1);
                break;
            case WALL:
                thing = new Wall(-1);
                break;
            case TWICE_BREAKABLE_WALL:
                thing = new TwiceBreakableWall(-1, scene);
                break;
            default:
                Log.app().error("PlayableSprite parseBytes(): illegal type number!");
                return null;
        }
        iStream.read(fourBytes); thing.id = DataUtils.bytesToInt(fourBytes);
        iStream.read(fourBytes); thing.blocking = (DataUtils.bytesToInt(fourBytes) == 1);
        for (byte[] infoBytes : Objects.requireNonNull(DataUtils.parseCollectionBytes(iStream))) {
            thing.tiles.add(Tile.parseBytes(infoBytes, scene));
        }
        if (type == TWICE_BREAKABLE_WALL) {
            iStream.read(fourBytes);
            ((TwiceBreakableWall) thing).lives = DataUtils.bytesToInt(fourBytes);
        }

        return thing;
    }

    public void updateFromAnotherThing(Thing thing) {
        this.blocking = thing.blocking;
    }

}
