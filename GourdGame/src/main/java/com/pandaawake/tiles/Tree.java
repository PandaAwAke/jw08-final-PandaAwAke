package com.pandaawake.tiles;

import com.pandaawake.utils.UtilFunctions;

public class Tree extends Thing {

    public Tree() {
        super(true);
        setTileTexture(UtilFunctions.PositionInTilesToIndex(7, 10));
    }

    @Override
    public boolean OnExplode() {
        return true;
    }

}
