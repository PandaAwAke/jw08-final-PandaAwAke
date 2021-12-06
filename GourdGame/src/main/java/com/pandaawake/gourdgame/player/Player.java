package com.pandaawake.gourdgame.player;

import com.mandas.tiled2d.event.KeyEvents;

import java.awt.event.KeyEvent;

public interface Player {

    public void setBomb();

    // ---------------------- Callback Functions ----------------------
    public void OnUpdate(float timestep);
    public void OnKeyPressed(KeyEvents.Pressed e);

}
