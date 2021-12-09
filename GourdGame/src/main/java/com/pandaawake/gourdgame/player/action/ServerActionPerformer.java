package com.pandaawake.gourdgame.player.action;

import java.util.ArrayList;
import java.util.List;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.main.ServerGameApp;
import com.pandaawake.gourdgame.player.Player;

public class ServerActionPerformer implements ActionPerformer {

    private ServerGameApp app;

    public ServerActionPerformer(ServerGameApp app) {
        this.app = app;
    }

    @Override
    public void performAction(Action action) {
        if (action instanceof PlayerAction) {
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
