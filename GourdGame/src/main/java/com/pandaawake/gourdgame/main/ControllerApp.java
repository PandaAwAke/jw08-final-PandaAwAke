package com.pandaawake.gourdgame.main;

import com.mandas.tiled2d.core.GameApplication;
import com.mandas.tiled2d.event.EventDispatcher;
import com.mandas.tiled2d.event.events.WindowEvents;
import com.pandaawake.gourdgame.Config;

public class ControllerApp implements GameApplication {

    private ClientGameApp clientGameApp = null;
    private ServerGameApp serverGameApp = null;
    private ReplayerApp replayerApp = null;

    public ControllerApp() {
        if (Config.ReplayMode) {
            replayerApp = new ReplayerApp();
        } else {
            if (Config.ServerMode) {
                serverGameApp = new ServerGameApp();
            }
            clientGameApp = new ClientGameApp();
        }

        EventDispatcher.register(WindowEvents.Closing.class, e -> {
            if (Config.ReplayMode) {
                replayerApp.OnDestroy();
            } else {
                if (serverGameApp != null) {
                    serverGameApp.OnDestroy();
                }
                clientGameApp.OnDestroy();
            }
        });
    }

    @Override
    public void InitRenderer() {
        if (Config.ReplayMode) {
            replayerApp.InitRenderer();
        } else {
            clientGameApp.InitRenderer();
        }
    }

    @Override
    public void OnRender() {
        if (Config.ReplayMode) {
            replayerApp.OnRender();
        } else {
            clientGameApp.OnRender();
        }
    }

    @Override
    public void OnUpdate(float timestep) {
        if (Config.ReplayMode) {
            replayerApp.OnUpdate(timestep);
        } else {
            if (serverGameApp != null) {
                serverGameApp.OnUpdate(timestep);
            }
            clientGameApp.OnUpdate(timestep);
        }
    }

}
