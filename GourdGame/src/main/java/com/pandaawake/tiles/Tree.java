package com.pandaawake.tiles;

import com.pandaawake.Config;
import com.pandaawake.utils.UtilFunctions;

public class Tree extends Thing {

    public Tree() {
        super(true);
        tileTextures.setTileTexture(Config.TileParser.getTile(7, 10));
    }

    @Override
    public boolean OnExplode() {
        return true;
    }

}
