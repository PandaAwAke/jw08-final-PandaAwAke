package com.pandaawake.gourdgame.network.data.performer;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.main.ClientGameApp;
import com.pandaawake.gourdgame.network.GameClient;
import com.pandaawake.gourdgame.network.data.action.ConnectionAction;
import com.pandaawake.gourdgame.network.data.action.GameAction;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;
import com.pandaawake.gourdgame.player.OtherPlayer;
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
            Log.app().info(getClass().getName() + ": Game started!");
        } else if (action instanceof GameAction.GamePause) {
            app.setPause(true);
            Log.app().info(getClass().getName() + ": Game paused!");
        } else if (action instanceof GameAction.GameResume) {
            app.setPause(false);
            Log.app().info(getClass().getName() + ": Game resumed!");
        } else if (action instanceof GameAction.GameEnd) {
            app.setPause(true);
            Log.app().info(getClass().getName() + ": Game ended!");
        } else if (action instanceof GameAction.GameInitialize) {
            if (app.getMainPlayerId() == -1) {
                Log.app().error(getClass().getName() + ": Initialize before Connection Verification!");
                return;
            }
            GameAction.GameInitialize initAction = (GameAction.GameInitialize) action;
            app.getPlayers().clear();
            app.getPlayers().addAll(initAction.players);
            for (Player player : app.getPlayers()) {
                app.getScene().getSceneUpdater().addSprite(player.sprite);
                if (player.id == app.getMainPlayerId()) {
                    app.setMainPlayer((OtherPlayer) player);
                }
            }
            Log.app().info(getClass().getName() + ": Initialized game players!");
        } else {
            Log.app().error(getClass().getName() + ": Null action or illegal/unsupported action!");
        }
    }

    @Override
    protected void performAction(ConnectionAction action) {
        if (action instanceof ConnectionAction.ClientSuccessfullyAccepted) {
            app.setMainPlayerId(((ConnectionAction.ClientSuccessfullyAccepted) action).playerId);
            Log.app().info(this.getClass().getName() + ": Successfully connected the server");
            Log.app().info(this.getClass().getName() + ": playerId = " + app.getMainPlayerId());
        } else if (action instanceof ConnectionAction.ClientUnsuccessfullyAccepted) {
            Log.app().fatal(this.getClass().getName() + ": Failed to connect to the GameSocketServer! " +
                    ((ConnectionAction.ClientUnsuccessfullyAccepted) action).errorReason);
        } else if (action instanceof ConnectionAction.ServerClosed) {
            Log.app().info("Server closed!");
            app.setPause(true);
        } else {
            Log.app().error(getClass().getName() + ": Null action or illegal/unsupported action!");
        }
    }

    @Override
    protected void performAction(PlayerAction action) {
        List<Player> matchedPlayers = new ArrayList<>();
        for (Player player : app.getPlayers()) {
            if (player.id == action.playerId) {
                matchedPlayers.add(player);
            }
        }

        if (matchedPlayers.size() != 1) {
            Log.app().error(getClass().getName() + ": No matched player?!");
            return;
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
        } else {
            Log.app().error(getClass().getName() + ": Null action or illegal/unsupported action!");
        }
    }

}
