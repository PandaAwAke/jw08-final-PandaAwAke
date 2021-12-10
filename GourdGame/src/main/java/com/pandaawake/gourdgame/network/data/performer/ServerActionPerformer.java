package com.pandaawake.gourdgame.network.data.performer;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.main.ServerGameApp;
import com.pandaawake.gourdgame.network.GameServer;
import com.pandaawake.gourdgame.network.data.action.ConnectionAction;
import com.pandaawake.gourdgame.network.data.action.GameAction;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;
import com.pandaawake.gourdgame.player.Player;

import java.util.ArrayList;
import java.util.List;

public class ServerActionPerformer extends ActionPerformer {

    private ServerGameApp app;
    private GameServer gameServer = null;

    public ServerActionPerformer(ServerGameApp app) {
        this.app = app;
    }

    public void setGameServer(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @Override
    protected void performAction(GameAction action) {
        Log.app().error(getClass().getName() + ": Null action or illegal/unsupported action!");
    }

    @Override
    protected void performAction(ConnectionAction action) {
        if (action instanceof ConnectionAction.ClientEnter) {
            // App: Allocate a Player for this client

        } else if (action instanceof ConnectionAction.ClientExit) {
            // App: Remove this id and Player for this client

        } else {
            Log.app().error(getClass().getName() + ": Null action or illegal/unsupported action!");
        }
    }

    @Override
    protected void performAction(PlayerAction action) {
        List<Player> matchedPlayers = new ArrayList<>();
        for (Player player : app.getPlayers()) {
            if (player.getId() == action.playerId) {
                matchedPlayers.add(player);
            }
        }

        assert matchedPlayers.size() == 1;
        Player matchedPlayer = matchedPlayers.get(0);

        if (action instanceof PlayerAction.NoAction) {
            // Do nothing
        } else if (action instanceof PlayerAction.DoMove) {
            assert ((PlayerAction.DoMove) action).direction != null;
            matchedPlayer.doMove(((PlayerAction.DoMove) action).direction);
        } else if (action instanceof PlayerAction.SetBomb) {
            matchedPlayer.setBomb();
        } else if (action instanceof PlayerAction.ExplodeBomb) {
            matchedPlayer.explodeBomb();
        } else {
            Log.app().error(getClass().getName() + ": Null action or illegal/unsupported action!");
        }
    }

}
