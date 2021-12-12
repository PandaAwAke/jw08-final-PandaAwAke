package com.pandaawake.gourdgame.main;

import com.mandas.tiled2d.core.GameApplication;
import com.mandas.tiled2d.event.EventDispatcher;
import com.mandas.tiled2d.event.WindowEvents;
import com.pandaawake.gourdgame.Config;

public class ControllerApp implements GameApplication {

    private ClientGameApp clientGameApp = null;
    private ServerGameApp serverGameApp = null;

    public ControllerApp() {
        if (Config.ServerMode) {
            serverGameApp = new ServerGameApp();
        }
        clientGameApp = new ClientGameApp();

        EventDispatcher.register(WindowEvents.Closing.class, e -> {
            if (serverGameApp != null) {
                serverGameApp.OnDestroy();
            }
            clientGameApp.OnDestroy();
        });
    }

    @Override
    public void InitRenderer() {
        clientGameApp.InitRenderer();
    }

    @Override
    public void OnRender() {
        clientGameApp.OnRender();
    }

    @Override
    public void OnUpdate(float timestep) {
        if (serverGameApp != null) {
            serverGameApp.OnUpdate(timestep);
        }
        clientGameApp.OnUpdate(timestep);
    }

}
