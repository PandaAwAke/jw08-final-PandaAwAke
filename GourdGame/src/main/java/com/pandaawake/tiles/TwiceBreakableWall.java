package com.pandaawake.tiles;

import com.pandaawake.utils.UtilFunctions;

public class TwiceBreakableWall extends Thing {

    private int lives = 2;

    public TwiceBreakableWall() {
        super(true);
        setTileTexture(UtilFunctions.PositionInTilesToIndex(7, 7));
    }

    @Override
    public boolean OnExplode() {
        if (lives > 0) {
            lives -= 1;
            if (lives == 1) {
                setTileTexture(UtilFunctions.PositionInTilesToIndex(6, 7));
            } else {
                setTileTexture(UtilFunctions.PositionInTilesToIndex(7, 6));
                blocking = false;
            }
        }
        return false;
    }

}
