package com.pandaawake.sprites;

import com.pandaawake.gamemap.Scene;
import com.pandaawake.utils.UtilFunctions;

public class CalabashPlayer extends Player {

    public CalabashPlayer(Scene scene) {
        super(scene, 1, 1);
        setTileTexture(UtilFunctions.PositionInTilesToChar(7, 0));

    }

}
