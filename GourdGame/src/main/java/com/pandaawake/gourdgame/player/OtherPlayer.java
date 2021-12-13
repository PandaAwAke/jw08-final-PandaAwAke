package com.pandaawake.gourdgame.player;

import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.sprites.Bomb;
import com.pandaawake.gourdgame.sprites.PlayableSprite;
import com.pandaawake.gourdgame.utils.Direction;

public class OtherPlayer extends Player {

    public OtherPlayer(PlayableSprite sprite, int id, String name) {
        super(sprite, id, name);
    }

    @Override
    public void OnUpdate(float timestep) {
        if (Config.ReplayMode) {
            return;
        }
    }

    @Override
    public boolean doMove(Direction direction) {
        return sprite.doMove(direction);
    }

    @Override
    public boolean setBomb() {
        return sprite.setNewBomb();
    }

    @Override
    public boolean explodeBomb() {
        for (Bomb bomb : sprite.getBombs()) {
            bomb.setExplodeImmediately();
        }
        return true;
    }

}
