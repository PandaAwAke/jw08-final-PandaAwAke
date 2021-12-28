package com.pandaawake.gourdgame.main;

import com.mandas.tiled2d.core.GameApplication;

public abstract class GameApp implements GameApplication {
    // ---------------------- GameApplication Functions ----------------------
    @Override
    public abstract void InitRenderer();

    @Override
    public abstract void OnRender();

    @Override
    public abstract void OnUpdate(float timestep);

    public abstract void OnDestroy();

}
