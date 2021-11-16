package com.pandaawake.player;

import java.awt.event.KeyEvent;

public interface Player {

    public void setBomb();

    // ---------------------- Callback Functions ----------------------
    public void OnUpdate(float timestep);
    public void OnKeyPressed(KeyEvent e);

}
