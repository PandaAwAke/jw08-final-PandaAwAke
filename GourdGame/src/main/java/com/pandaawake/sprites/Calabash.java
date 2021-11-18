package com.pandaawake.sprites;

import com.pandaawake.Config;
import com.pandaawake.scene.Scene;
import com.pandaawake.utils.UtilFunctions;

import java.util.HashSet;
import java.util.Set;

public class Calabash extends MovableSprite implements HasBomb {

    private int lives = Config.HumanPlayerLives;
    private Set<Bomb> bombs = new HashSet<>();

    public Calabash(Scene scene) {
        super(true, scene, Config.HumanPlayerMovingSpeed, 1, 1);
        tileTextures.setTileTexture(Config.TileParser.getTile(6, 8));
    }

    public int getLives() {
        return lives;
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
        if (canSetBomb()) {
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
