package com.pandaawake.gourdgame.tiles;

import com.pandaawake.gourdgame.Config;

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
