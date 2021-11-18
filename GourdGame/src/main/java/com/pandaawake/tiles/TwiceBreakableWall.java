package com.pandaawake.tiles;

import com.pandaawake.Config;
import com.pandaawake.utils.UtilFunctions;

public class TwiceBreakableWall extends Thing {

    private int lives = 2;

    public TwiceBreakableWall() {
        super(true);
        tileTextures.setTileTexture(Config.TileParser.getTile(7, 7));
    }

    @Override
    public boolean OnExplode() {
        if (lives > 0) {
            lives -= 1;
            if (lives == 1) {
                tileTextures.setTileTexture(Config.TileParser.getTile(6, 7));
            } else {
                tileTextures.setTileTexture(Config.TileParser.getTile(7, 6));
                blocking = false;
            }
        }
        return false;
    }

}
