package com.pandaawake.sprites;

import com.pandaawake.scene.Scene;
import com.pandaawake.utils.UtilFunctions;

public class Bomb2 extends Bomb {


    /**
     * Bomb is a 3x3 destroyer!
     *
     * @param scene
     * @param owner
     * @param posX
     * @param posY
     */
    public Bomb2(Scene scene, HasBomb owner, float posX, float posY) {
        super(scene, owner, posX, posY);
        setTileTexture(UtilFunctions.PositionInTilesToIndex(6, 6));
    }
}
