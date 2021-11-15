package com.pandaawake.tiles;

import com.pandaawake.utils.UtilFunctions;

public class Floor extends Thing {

    public Floor() {
        super(false);
        setTileTexture(UtilFunctions.PositionInTilesToChar(5, 12));
    }

}
