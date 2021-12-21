package com.pandaawake.gourdgame.tiles;

import com.pandaawake.gourdgame.Config;

public class Wall extends Thing {

    public Wall(int id) {
        super(id, true);
        tileTextures.setTileTexture(Config.TileParser.getTile(6, 10));
    }

}
