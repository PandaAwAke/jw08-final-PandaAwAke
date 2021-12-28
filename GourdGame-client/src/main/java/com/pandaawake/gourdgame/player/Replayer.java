package com.pandaawake.gourdgame.player;

import com.mandas.tiled2d.Config;
import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.main.ReplayerApp;
import com.pandaawake.gourdgame.network.data.action.Action;
import com.pandaawake.gourdgame.network.data.action.GameAction;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;
import com.pandaawake.gourdgame.network.data.performer.ActionPerformer;
import com.pandaawake.gourdgame.network.data.performer.ReplayerActionPerformer;
import com.pandaawake.gourdgame.utils.GameTraceParser;

import java.io.IOException;

public class Replayer {

    private final GameTraceParser parser;
    private final ActionPerformer performer;

    private float elapsedTime = 0.0f;
    private Action nextAction = null;
    private boolean running = false;
    
    private float nextActionDeltaTime = 1.0f;   // Initialize value
    private ReplayerApp app;

    public Replayer(ReplayerApp app) {
        this.app = app;
        parser = new GameTraceParser(Config.LogFilename);
        performer = new ReplayerActionPerformer(app);
        Init();
    }

    private void Init() {
        getNextAction();
        while (!(nextAction instanceof GameAction.GameStart)) {
            GameAction.GameInitialize initAction = (GameAction.GameInitialize) nextAction;
            performer.performAction(initAction);
            getNextAction();
        }
        getNextAction();
        running = true;
        app.setPause(false);
    }

    public void getNextAction() {
        Action tempNextAction = parser.getAnAction();
        if (tempNextAction == null) {
            running = false;
            app.setPause(true);
            return;
        }

        if (nextAction == null) {
            nextAction = tempNextAction;
            return;
        }

        if (nextAction instanceof PlayerAction && tempNextAction instanceof PlayerAction) {
            long deltaTime = ((PlayerAction) tempNextAction).time.getTime() - ((PlayerAction) nextAction).time.getTime();
            nextActionDeltaTime = (float) deltaTime / 1000.0f;
        }

        nextAction = tempNextAction;
    }

    public void OnUpdate(float timestep) {
        if (!running) {
            return;
        }
        elapsedTime += timestep;
        if (elapsedTime >= nextActionDeltaTime) {
            elapsedTime = 0.0f;
            if (nextAction != null) {
                performer.performAction(nextAction);
            }
            getNextAction();
        }
    }

    public void OnDestroy() {
        try {
            parser.Close();
        } catch (IOException e) {
            Log.app().error("IOException when replayer tried to close the log file!");
            e.printStackTrace();
        }
    }

}
