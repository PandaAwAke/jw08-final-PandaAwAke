package com.mandas.tiled2d.core;

import java.awt.event.KeyEvent;

public interface GameApplication {

    public void OnRender();
    public void OnUpdate(float timestep);
    public void OnKeyPressed(KeyEvent e);

}
