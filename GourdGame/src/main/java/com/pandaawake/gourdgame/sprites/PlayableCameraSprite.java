package com.pandaawake.gourdgame.sprites;

import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.scene.Scene;

public abstract class PlayableCameraSprite extends PlayableSprite {

    public PlayableCameraSprite(int id, int lives, int maxBombs, boolean blocking, Scene scene, float movingSpeed, int spriteWidth, int spriteHeight) {
        super(id, lives, maxBombs, blocking, scene, movingSpeed, spriteWidth, spriteHeight);
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

}
