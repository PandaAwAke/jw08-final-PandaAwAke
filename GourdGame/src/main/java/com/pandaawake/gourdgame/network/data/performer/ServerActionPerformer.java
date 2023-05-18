package com.pandaawake.gourdgame.network.data.performer;

import com.mandas.tiled2d.core.Log;
import com.mandas.tiled2d.utils.pair.Pair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.main.ServerGameApp;
import com.pandaawake.gourdgame.network.GameServer;
import com.pandaawake.gourdgame.network.data.action.ConnectionAction;
import com.pandaawake.gourdgame.network.data.action.GameAction;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;
import com.pandaawake.gourdgame.network.data.action.SceneAction;
import com.pandaawake.gourdgame.player.HumanPlayer;
import com.pandaawake.gourdgame.player.Player;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.sprites.Calabash;
import com.pandaawake.gourdgame.utils.Direction;

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
            Log.app().info("Client " + action.senderClientId + " Enter");
            // App: Allocate a Player for this client
            int spriteId = Scene.getNextSpriteId();
            String playerName = Config.level.humanPlayerNames.get(action.senderClientId);
            app.getClientIdsAndSpriteIdsNames().put(action.senderClientId, new Pair<>(spriteId, playerName));
            Calabash humanCalabash = new Calabash(spriteId, app.getScene());
            humanCalabash.setPos(Config.level.humanPlayerPositions.get(action.senderClientId).first,
                    Config.level.humanPlayerPositions.get(action.senderClientId).second);
            app.getScene().getSceneUpdater().addSprite(humanCalabash);
            app.getPlayers().add(new HumanPlayer(humanCalabash, action.senderClientId, playerName));

            Log.file().trace("" + action.senderClientId + " " + spriteId + " " + playerName);

            gameServer.sendAction(new ConnectionAction.ClientSuccessfullyAccepted(-1, action.senderClientId), action.senderClientId);
        } else if (action instanceof ConnectionAction.ClientExit) {
            // App: Remove this id and Player for this client
            //app.getPlayers().removeIf(player -> player.id == action.senderClientId);
        } else {
            Log.app().error(getClass().getName() + ": Null action or illegal/unsupported action!");
        }
    }

    @Override
    protected void performAction(PlayerAction action) {
        List<Player> matchedPlayers = new ArrayList<>();
        for (Player player : app.getPlayers()) {
            if (player.sprite.getId() == action.spriteId) {
                matchedPlayers.add(player);
            }
        }

        if (matchedPlayers.size() != 1) {
            Log.app().error("No matched player?");
            return;
        }

        Player matchedPlayer = matchedPlayers.get(0);

        if (action instanceof PlayerAction.NoAction) {
            // Do nothing
        } else if (action instanceof PlayerAction.DoMove) {
            Direction direction = ((PlayerAction.DoMove) action).direction;
            if (direction == null) {
                Log.app().error("Null direction?!");
            }
            if (matchedPlayer.doMove(direction)) {
                Log.app().trace("Server: Player " + matchedPlayer.id + " DoMove " + direction.toString());
                Log.file().trace(action.toString());
                //gameServer.sendAction(action);
            }
        } else if (action instanceof PlayerAction.SetBomb) {
            if (matchedPlayer.canSetBomb()) {
                matchedPlayer.setBomb();
                Log.app().trace("Server: Player " + matchedPlayer.id + " SetBomb");
                Log.file().trace(action.toString());
                //gameServer.sendAction(action);
            }
        } else if (action instanceof PlayerAction.ExplodeBomb) {
            if (matchedPlayer.explodeBomb()) {
                Log.app().trace("Server: Player " + matchedPlayer.id + " ExplodeBomb");
                Log.file().trace(action.toString());
                //gameServer.sendAction(action);
            }
        } else {
            Log.app().error(getClass().getName() + ": Null action or illegal/unsupported action!");
        }


    }

    @Override
    protected void performAction(SceneAction action) {
        Log.app().error(getClass().getName() + ": Null action or illegal/unsupported action!");
    }

}
