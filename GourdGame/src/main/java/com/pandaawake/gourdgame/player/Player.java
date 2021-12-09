package com.pandaawake.gourdgame.player;

import com.mandas.tiled2d.event.KeyEvents;
import com.pandaawake.gourdgame.sprites.PlayableSprite;
import com.pandaawake.gourdgame.utils.Direction;

public abstract class Player {

    protected PlayableSprite sprite;

    public Player(PlayableSprite sprite) {
        this.sprite = sprite;
    }

    public PlayableSprite getSprite() {
        return sprite;
    }

    public abstract String getName();

    public abstract void doMove(Direction direction);
    public abstract void setBomb();
    public abstract void explodeBomb();

    // ---------------------- Callback Functions ----------------------
    public abstract void OnUpdate(float timestep);
    public abstract void OnKeyPressed(KeyEvents.Pressed e);

    public byte[] toBytes() {
        // TODO
        return null;
    }

    public static Player parseBytes(byte[] bytes) {
        // TODO
        return null;
    }

}
