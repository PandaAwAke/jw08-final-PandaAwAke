package com.pandaawake.gourdgame.network.data.performer;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.network.data.action.*;

public abstract class ActionPerformer {
    
    public void performAction(Action action) {
        if (action instanceof GameAction) {
            performAction((GameAction) action);
        } else if (action instanceof ConnectionAction) {
            performAction((ConnectionAction) action);
        } else if (action instanceof PlayerAction) {
            performAction((PlayerAction) action);
        } else if (action instanceof SceneAction) {
            performAction((SceneAction) action);
        } else {
            Log.app().error(getClass().getName() + ": Null action or unsupported action!");
        }
    }

    abstract protected void performAction(GameAction action);
    abstract protected void performAction(ConnectionAction action);
    abstract protected void performAction(PlayerAction action);
    abstract protected void performAction(SceneAction action);

}
