package com.pandaawake.gourdgame;

import com.pandaawake.sprites.MovableSprite;
import com.pandaawake.utils.Direction;

import java.awt.event.KeyEvent;

public interface Player {

    public void tryMove();
    public void setBomb();

    // ---------------------- Callback Functions ----------------------
    public void OnUpdate(float timestep);
    public void OnKeyPressed(KeyEvent e);

}
