package com.pandaawake.sprites;

import com.mandas.tiled2d.utils.FloatPair;
import com.pandaawake.Config;
import com.pandaawake.scene.Scene;

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
        getTileTextureRenderComponent().getPositionsAndTextures().clear();
        getTileTextureRenderComponent().addPositionAndTexture(new FloatPair(0, 0), Config.TileParser.getTile(6, 6));
    }
}
