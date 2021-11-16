package com.pandaawake.sprites;

import com.pandaawake.Config;
import com.pandaawake.scene.Scene;
import com.pandaawake.utils.UtilFunctions;

import java.util.HashSet;
import java.util.Set;

public class Snake extends MovableSprite implements HasBomb {

    private int lives = Config.ComputerPlayerLives;
    private Set<Bomb> bombs = new HashSet<>();

    public Snake(Scene scene) {
        super(true, scene, Config.ComputerPlayerMovingSpeed, 1, 1);
        setTileTexture(UtilFunctions.PositionInTilesToIndex(6, 9));
    }

    public int getLives() {
        return lives;
    }

    @Override
    public boolean OnExplode(Bomb bomb) {
        if (bomb.getOwner() == this) {
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
