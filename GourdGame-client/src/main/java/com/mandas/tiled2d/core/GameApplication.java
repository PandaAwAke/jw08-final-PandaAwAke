package com.mandas.tiled2d.core;

public interface GameApplication {

    void InitRenderer();
    void OnRender();
    void OnUpdate(float timestep);

}
