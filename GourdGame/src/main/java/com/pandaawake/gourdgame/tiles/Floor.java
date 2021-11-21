package com.pandaawake.gourdgame.tiles;

import com.pandaawake.gourdgame.Config;

public class Floor extends Thing {

    public Floor() {
        super(false);
        tileTextures.setTileTexture(Config.TileParser.getTile(7, 9));
    }

}
