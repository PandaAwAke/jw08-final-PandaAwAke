package com.pandaawake.gourdgame.player;

import com.mandas.tiled2d.event.KeyEvents;
import com.pandaawake.gourdgame.utils.Direction;

public interface Player {

    public String getName();

    public void doMove(Direction direction);
    public void setBomb();
    public void explodeBomb();

    // ---------------------- Callback Functions ----------------------
    public void OnUpdate(float timestep);
    public void OnKeyPressed(KeyEvents.Pressed e);

}
