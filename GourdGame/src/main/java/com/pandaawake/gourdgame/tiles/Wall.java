package com.pandaawake.gourdgame.tiles;

import com.pandaawake.gourdgame.Config;

public class Wall extends Thing {

    public Wall() {
        super(true);
        tileTextures.setTileTexture(Config.TileParser.getTile(6, 10));
    }

}
