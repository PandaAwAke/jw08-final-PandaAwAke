package com.pandaawake.gourdgame.sprites;

import com.mandas.tiled2d.utils.FloatPair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.scene.Scene;

import java.util.HashSet;
import java.util.Set;

public class Snake extends MovableSprite implements HasBomb {

    private int lives = Config.ComputerPlayerLives;
    private Set<Bomb> bombs = new HashSet<>();

    public Snake(Scene scene) {
        super(true, scene, Config.ComputerPlayerMovingSpeed, 1, 1);
        getTileTextureRenderComponent().addPositionAndTexture(new FloatPair(0, 0), Config.TileParser.getTile(6, 9));
    }

    public int getLives() {
        return lives;
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
    public Set<Bomb> getBombs() {
        return bombs;
    }

    @Override
    public boolean canSetBomb() {
        return bombs.size() < Config.ComputerPlayerBombs;
    }

    @Override
    public void setNewBomb() {
        if (canSetBomb()) {
            Bomb bomb = new Bomb2(scene, this, posX, posY);
            bombs.add(bomb);
            scene.addSprite(bomb);
        }
    }

    @Override
    public void bombDestroyed(Bomb bomb) {
        bombs.remove(bomb);
    }

}
