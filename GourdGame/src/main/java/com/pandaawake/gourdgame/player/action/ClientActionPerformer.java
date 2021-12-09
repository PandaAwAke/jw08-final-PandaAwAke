package com.pandaawake.gourdgame.player.action;

import java.util.ArrayList;
import java.util.List;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.main.ClientGameApp;
import com.pandaawake.gourdgame.player.Player;

public class ClientActionPerformer implements ActionPerformer {

    private ClientGameApp app;

    public ClientActionPerformer(ClientGameApp app) {
        this.app = app;
    }

    @Override
    public void performAction(Action action) {
        if (action instanceof Actions.GameStart) {
            app.setPause(false);
            // TODO: Other things?
        } else if (action instanceof Actions.GamePause) {
            app.setPause(true);
        } else if (action instanceof Actions.GameResume) {
            app.setPause(false);
        } else if (action instanceof Actions.GameEnd) {
            app.setPause(true);
            // TODO: Other things?
        } else if (action instanceof Actions.GameInitialize) {
            Actions.GameInitialize initAction = (Actions.GameInitialize) action;
            // TODO
        } else if (action instanceof PlayerAction) {
            List<Player> matchedPlayers = new ArrayList<>();
            for (Player player : app.getPlayers()) {
                if (player.getName().equals(((PlayerAction) action).playerName)) {
                    matchedPlayers.add(player);
                }
            }
    
            assert matchedPlayers.size() == 1;
            Player matchedPlayer = matchedPlayers.get(0);
    
            if (action instanceof Actions.PlayerNoAction) {
                // Do nothing
            } else if (action instanceof Actions.PlayerDoMove) {
                assert ((Actions.PlayerDoMove) action).direction != null;
                matchedPlayer.doMove(((Actions.PlayerDoMove) action).direction);
            } else if (action instanceof Actions.PlayerSetBomb) {
                matchedPlayer.setBomb();
            } else if (action instanceof Actions.PlayerExplodeBomb) {
                matchedPlayer.explodeBomb();
            } else {

            }
        } else {
            Log.app().error(this.getClass().getName() + ": Null action or unsupported Action!");
        }
    }
    
}
