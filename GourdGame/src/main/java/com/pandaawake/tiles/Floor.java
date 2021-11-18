package com.pandaawake.tiles;

import com.pandaawake.Config;

public class Floor extends Thing {

    public Floor() {
        super(false);
        tileTextures.setTileTexture(Config.TileParser.getTile(7, 9));
    }

}
