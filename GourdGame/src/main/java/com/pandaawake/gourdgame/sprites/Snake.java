package com.pandaawake.gourdgame.sprites;

import com.mandas.tiled2d.utils.FloatPair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.scene.Scene;

public class Snake extends PlayableSprite {

    public Snake(int id, Scene scene) {
        super(id, Config.ComputerPlayerLives, Config.ComputerPlayerBombs, true, scene, Config.ComputerPlayerMovingSpeed, 1, 1);
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
    public boolean setNewBomb() {
        if (canSetBomb()) {
            Bomb bomb = new Bomb2(Scene.getNextSpriteId(), scene, id, posX, posY);
            bombs.add(bomb);
            scene.getSceneUpdater().addSprite(bomb);
            return true;
        }
        return false;
    }

}
