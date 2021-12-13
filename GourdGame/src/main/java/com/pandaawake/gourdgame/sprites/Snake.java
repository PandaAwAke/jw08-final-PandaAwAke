package com.pandaawake.gourdgame.sprites;

import com.mandas.tiled2d.utils.FloatPair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.scene.Scene;

public class Snake extends PlayableSprite {

    public Snake(Scene scene) {
        super(Config.ComputerPlayerLives, Config.ComputerPlayerBombs, true, scene, Config.ComputerPlayerMovingSpeed, 1, 1);
        getTileTextureRenderComponent().addPositionAndTexture(new FloatPair(0, 0), Config.TileParser.getTile(6, 9));
    }

    @Override
    public boolean OnExplode(Bomb bomb) {
        if (bomb.getOwner() instanceof Snake) {
            return false;
        }
        lives -= 1;
        if (lives <= 0) {
            return true;
        }
        return false;
    }


    @Override
    public void setNewBomb() {
        if (status == MovableSprite.Status.Ok && canSetBomb()) {
            Bomb bomb = new Bomb2(scene, this, posX, posY);
            bombs.add(bomb);
            scene.getSceneUpdater().addSprite(bomb);
        }
    }

}