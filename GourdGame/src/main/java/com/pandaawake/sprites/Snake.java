package com.pandaawake.sprites;

import com.pandaawake.scene.Scene;
import com.pandaawake.utils.UtilFunctions;

public class Snake extends MovableSprite implements HasBomb {

    private int life = 2;
    private int bombs = 2;

    public Snake(Scene scene) {
        super(true, scene, 1, 1);
        setTileTexture(UtilFunctions.PositionInTilesToIndex(6, 9));
    }

    @Override
    public boolean OnExplode(Bomb bomb) {
        if (bomb.getOwner() == this) {
            return false;
        }
        life -= 1;
        if (life <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean setBomb() {
        if (bombs > 0) {
            bombs -= 1;
            return true;
        }
        return false;
    }

    @Override
    public void bombDestroyed() {
        bombs += 1;
        if (bombs > 2) {
            bombs = 2;
        }
    }

}
