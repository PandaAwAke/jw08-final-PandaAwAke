package com.pandaawake.tiles;

import com.pandaawake.Config;
import com.pandaawake.utils.UtilFunctions;

public class Wall extends Thing {

    public Wall() {
        super(true);
        tileTextures.setTileTexture(Config.TileParser.getTile(6, 10));
    }

}
