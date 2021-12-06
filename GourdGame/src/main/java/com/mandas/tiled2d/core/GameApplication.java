package com.mandas.tiled2d.core;

public interface GameApplication {

    public void InitRenderer();
    public void OnRender();
    public void OnUpdate(float timestep);

}
