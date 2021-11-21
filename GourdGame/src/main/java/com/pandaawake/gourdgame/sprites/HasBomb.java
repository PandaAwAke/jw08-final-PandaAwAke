package com.pandaawake.gourdgame.sprites;

import java.util.Set;

public interface HasBomb {

    public Set<Bomb> getBombs();
    public boolean canSetBomb();
    public void setNewBomb();
    public void bombDestroyed(Bomb bomb);

}
