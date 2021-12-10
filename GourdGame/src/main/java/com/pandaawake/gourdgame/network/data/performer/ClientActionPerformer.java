package com.pandaawake.gourdgame.network.data.performer;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.main.ClientGameApp;
import com.pandaawake.gourdgame.network.GameClient;
import com.pandaawake.gourdgame.network.data.action.ConnectionAction;
import com.pandaawake.gourdgame.network.data.action.GameAction;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;
import com.pandaawake.gourdgame.player.Player;

import java.util.ArrayList;
import java.util.List;

public class ClientActionPerformer extends ActionPerformer {

    private ClientGameApp app;
    private GameClient gameClient;

    public ClientActionPerformer(ClientGameApp app) {
        this.app = app;
    }

    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
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
        } else {
            Log.app().error(getClass().getName() + ": Null action or illegal/unsupported action!");
        }
    }

    @Override
    protected void performAction(ConnectionAction action) {
        if (action instanceof ConnectionAction.ClientSuccessfullyAccepted) {
            Log.app().info(this.getClass().getName() + ": Successfully connected the server");
        } else if (action instanceof ConnectionAction.ClientUnsuccessfullyAccepted) {
            Log.app().fatal(this.getClass().getName() + ": Failed to connect to the GameSocketServer! " +
                    ((ConnectionAction.ClientUnsuccessfullyAccepted) action).errorReason);
        } else if (action instanceof ConnectionAction.ServerClosed) {
            Log.app().info("Server closed!");
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
