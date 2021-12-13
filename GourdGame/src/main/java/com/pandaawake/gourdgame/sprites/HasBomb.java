package com.pandaawake.gourdgame.sprites;

import java.util.Set;

public interface HasBomb {

    Set<Bomb> getBombs();
    boolean canSetBomb();
    boolean setNewBomb();
    void bombDestroyed(Bomb bomb);

}
