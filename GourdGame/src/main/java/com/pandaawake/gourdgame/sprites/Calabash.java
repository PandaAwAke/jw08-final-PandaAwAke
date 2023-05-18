package com.pandaawake.gourdgame.sprites;

import com.mandas.tiled2d.renderer.Camera;
import com.mandas.tiled2d.scene.component.CameraComponent;
import com.mandas.tiled2d.scene.component.ComponentFactory;
import com.mandas.tiled2d.utils.pair.FloatPair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.scene.Scene;

public class Calabash extends PlayableSprite {

    public Calabash(int id, Scene scene) {
        super(id, Config.HumanPlayerLives, Config.HumanPlayerBombs, true, scene, Config.HumanPlayerMovingSpeed, 1, 1);
        getTileTextureRenderComponent().addPositionAndTexture(new FloatPair(0, 0), Config.TileParser.getTile(6, 8));
        addComponent(ComponentFactory.createComponent(CameraComponent.class,
                new Camera(Config.RenderWidth, Config.RenderHeight), false));
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        float cameraTranslationX = x - (float) Config.RenderWidth / 2;
        if (cameraTranslationX > 0) {
            getCameraComponent().getCamera().setTranslationX(Math.min(cameraTranslationX, Config.MapWidth - Config.RenderWidth));
        }
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        float cameraTranslationY = y - (float) Config.RenderHeight / 2;
        if (cameraTranslationY > 0) {
            getCameraComponent().getCamera().setTranslationY(Math.min(cameraTranslationY, Config.MapHeight - Config.RenderHeight));
        }
    }


    @Override
    public boolean OnExplode(Bomb bomb) {
        lives -= 1;
        if (lives <= 0) {
            return true;
        }
        return false;
    }


    @Override
    public boolean setNewBomb() {
        if (canSetBomb()) {
            Bomb bomb = new Bomb(Scene.getNextSpriteId(), scene, id, posX, posY);
            bombs.add(bomb);
            scene.getSceneUpdater().addSprite(bomb);
            return true;
        }
        return false;
    }

}
