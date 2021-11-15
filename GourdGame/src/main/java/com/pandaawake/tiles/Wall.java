package com.pandaawake.tiles;

import com.pandaawake.utils.UtilFunctions;

public class Wall extends Thing {

    public Wall() {
        super(true);
        setTileTexture(UtilFunctions.PositionInTilesToIndex(13, 7));
    }

}
