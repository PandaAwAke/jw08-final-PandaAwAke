package com.pandaawake.gourdgame.sprites;

import com.mandas.tiled2d.renderer.Camera;
import com.mandas.tiled2d.scene.CameraComponent;
import com.mandas.tiled2d.utils.FloatPair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.scene.Scene;

import java.util.HashSet;
import java.util.Set;

public class Calabash extends PlayableSprite implements HasBomb {

    private int lives = Config.HumanPlayerLives;
    private Set<Bomb> bombs = new HashSet<>();

    public Calabash(Scene scene) {
        super(true, scene, Config.HumanPlayerMovingSpeed, 1, 1);
        getTileTextureRenderComponent().addPositionAndTexture(new FloatPair(0, 0), Config.TileParser.getTile(6, 8));
        addComponent(new CameraComponent(new Camera(Config.RenderWidth, Config.RenderHeight), true));
    }

    public int getLives() {
        return lives;
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
    public Set<Bomb> getBombs() {
        return bombs;
    }

    @Override
    public boolean canSetBomb() {
        return bombs.size() < Config.HumanPlayerBombs;
    }

    @Override
    public void setNewBomb() {
        if (status == MovableSprite.Status.Ok && canSetBomb()) {
            Bomb bomb = new Bomb(scene, this, posX, posY);
            bombs.add(bomb);
            scene.addSprite(bomb);
        }
    }

    @Override
    public void bombDestroyed(Bomb bomb) {
        bombs.remove(bomb);
    }
}
