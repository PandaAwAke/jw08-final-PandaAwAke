package com.pandaawake.gourdgame.network.data.performer;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.main.ClientGameApp;
import com.pandaawake.gourdgame.network.data.action.ConnectionAction;
import com.pandaawake.gourdgame.network.data.action.GameAction;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;
import com.pandaawake.gourdgame.player.Player;

import java.util.ArrayList;
import java.util.List;

public class ReplayerActionPerformer extends ActionPerformer {

    private ClientGameApp app;

    public ReplayerActionPerformer(ClientGameApp app) {
        this.app = app;
    }


    @Override
    protected void performAction(GameAction action) {
        if (action instanceof GameAction.GameStart) {
            app.setPause(false);
            // TODO: Other things?
        } else if (action instanceof GameAction.GamePause) {
            app.setPause(true);
        } else if (action instanceof GameAction.GameResume) {
            app.setPause(false);
        } else if (action instanceof GameAction.GameEnd) {
            app.setPause(true);
            // TODO: Other things?
        } else if (action instanceof GameAction.GameInitialize) {
            GameAction.GameInitialize initAction = (GameAction.GameInitialize) action;
            // TODO
        }
    }

    @Override
    protected void performAction(ConnectionAction action) {

    }

    @Override
    protected void performAction(PlayerAction action) {
        List<Player> matchedPlayers = new ArrayList<>();
        for (Player player : app.getPlayers()) {
            if (player.id == action.playerId) {
                matchedPlayers.add(player);
            }
        }

        if (matchedPlayers.size() > 1) {
            Log.app().warn(getClass().getName() + ": Multiple players have the same id!");
        }

        if (matchedPlayers.size() == 0) {
            Log.app().error(getClass().getName() + ": No player has id " + action.playerId + "!");
        }

        Player matchedPlayer = matchedPlayers.get(0);

        if (action instanceof PlayerAction.NoAction) {
            // Do nothing
        } else if (action instanceof PlayerAction.DoMove) {
            if (((PlayerAction.DoMove) action).direction == null) {
                Log.app().error("Null direction?!");
            }
            matchedPlayer.doMove(((PlayerAction.DoMove) action).direction);
        } else if (action instanceof PlayerAction.SetBomb) {
            matchedPlayer.setBomb();
        } else if (action instanceof PlayerAction.ExplodeBomb) {
            matchedPlayer.explodeBomb();
        }
    }

}
