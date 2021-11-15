package com.pandaawake.tiles;

import com.pandaawake.utils.UtilFunctions;

public class Wall extends Thing {

    public Wall() {
        super(true);
        setTileTexture(UtilFunctions.PositionInTilesToChar(13, 7));
    }

}
