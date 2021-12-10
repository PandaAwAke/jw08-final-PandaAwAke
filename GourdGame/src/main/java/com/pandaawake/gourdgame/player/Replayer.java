package com.pandaawake.gourdgame.player;

import com.mandas.tiled2d.Config;
import com.pandaawake.gourdgame.main.GameApp;
import com.pandaawake.gourdgame.network.data.performer.ActionPerformer;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;
import com.pandaawake.gourdgame.network.data.performer.ReplayerActionPerformer;
import com.pandaawake.gourdgame.utils.GameTraceParser;

public class Replayer {

    private GameTraceParser parser;
    private ActionPerformer performer;

    private float elapsedTime = 0.0f;
    private PlayerAction playerAction = null;
    private boolean running = true;
    
    private float nextActionDeltaTime = 1.0f;   // Initialize value
    private GameApp app;

    public Replayer(GameApp app) {
        this.app = app;
        parser = new GameTraceParser(Config.LogFilename);
        performer = new ReplayerActionPerformer(app);
        getNextAction();
    }

    public void getNextAction() {
        PlayerAction nextAction = parser.getAnAction();
        if (nextAction == null) {
            running = false;
            return;
        }

        if (playerAction == null) {
            playerAction = nextAction;
            return;
        }

        long deltaTime = nextAction.time.getTime() - playerAction.time.getTime();
        nextActionDeltaTime = (float) deltaTime / 1000.0f;

        playerAction = nextAction;
    }

    public void OnUpdate(float timestep) {
        if (!running) {
            app.setPause(true);
            return;
        }
        elapsedTime += timestep;
        if (elapsedTime >= nextActionDeltaTime) {
            elapsedTime = 0.0f;
            performer.performAction(playerAction);
            getNextAction();
        }
    }

}
