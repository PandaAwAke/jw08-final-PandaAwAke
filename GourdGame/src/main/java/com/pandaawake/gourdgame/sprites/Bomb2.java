package com.pandaawake.gourdgame.sprites;

import com.mandas.tiled2d.utils.pair.FloatPair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.scene.Scene;

public class Bomb2 extends Bomb {


    /**
     * Bomb is a 3x3 destroyer!
     *
     * @param scene
     * @param ownerId
     * @param posX
     * @param posY
     */
    public Bomb2(int id, Scene scene, int ownerId, float posX, float posY) {
        super(id, scene, ownerId, posX, posY);
        getTileTextureRenderComponent().getPositionsAndTextures().clear();
        getTileTextureRenderComponent().addPositionAndTexture(new FloatPair(0, 0), Config.TileParser.getTile(6, 6));
    }
}
