package com.pandaawake.gourdgame.tiles;

import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.utils.DataUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TwiceBreakableWall extends Thing {

    public int lives = 2;
    protected Scene scene;

    public TwiceBreakableWall(int id, Scene scene) {
        super(id, true);
        this.scene = scene;
        tileTextures.setTileTexture(Config.TileParser.getTile(7, 7));
    }

    void refreshTiles() {
        if (lives == 1) {
            tileTextures.setTileTexture(Config.TileParser.getTile(6, 7));
        } else {
            tileTextures.setTileTexture(Config.TileParser.getTile(7, 6));
            blocking = false;
        }
    }

    @Override
    public boolean OnExplode() {
        if (lives > 0) {
            lives -= 1;
            scene.getSceneUpdater().addUpdateThing(this);
            refreshTiles();
        }
        return false;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();

        oStream.write(super.toBytes());
        oStream.write(DataUtils.intToBytes(lives));

        return oStream.toByteArray();
    }

    @Override
    public void updateFromAnotherThing(Thing thing) {
        super.updateFromAnotherThing(thing);
        this.lives = ((TwiceBreakableWall) thing).lives;
        refreshTiles();
    }

}
