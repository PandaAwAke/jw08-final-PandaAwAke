package com.pandaawake.gourdgame.network.data.performer;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.main.ClientGameApp;
import com.pandaawake.gourdgame.network.GameClient;
import com.pandaawake.gourdgame.network.data.action.Action;
import com.pandaawake.gourdgame.network.data.action.*;
import com.pandaawake.gourdgame.sprites.Sprite;
import com.pandaawake.gourdgame.tiles.Thing;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ClientActionPerformer extends ActionPerformer {

    private ClientGameApp app;
    private GameClient gameClient;
    private final List<Action> actionsRemaining;

    public ClientActionPerformer(ClientGameApp app) {
        this.app = app;
        actionsRemaining = new ArrayList<>();
    }

    public void handleRemainActions() {
        List<Action> remainActions = new ArrayList<>(actionsRemaining);
        actionsRemaining.clear();
        for (Action action : remainActions) {
            performAction(action);
        }
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
            if (((GameAction.GameEnd) action).humanWins) {
                JOptionPane.showMessageDialog(null, "游戏结束！你们赢了！", "信息", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "游戏结束！你们输了！", "信息", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (action instanceof GameAction.GameInitialize) {
            if (app.getClientId() == -1) {
                Log.app().error(getClass().getName() + ": Initialize before Connection Verification!");
                return;
            }
            GameAction.GameInitialize initAction = (GameAction.GameInitialize) action;
            
            app.resetAll();

            //app.getScene().getSceneUpdater().addSprite(player.sprite);

            app.setMainPlayerSpriteId(initAction.spriteId);
            app.setMainPlayerName(initAction.playerName);

            Log.app().info(getClass().getName() + ": Initialized game players!");
        } else {
            Log.app().error(getClass().getName() + ": Null action or illegal/unsupported action!");
        }
    }

    @Override
    protected void performAction(ConnectionAction action) {
        if (action instanceof ConnectionAction.ClientSuccessfullyAccepted) {
            app.setClientId(((ConnectionAction.ClientSuccessfullyAccepted) action).playerId);
            Log.app().info(this.getClass().getName() + ": Successfully connected the server");
            Log.app().info(this.getClass().getName() + ": clientId = " + app.getClientId());
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
        Sprite matchedSprite = null;

        for (Sprite sprite : app.getScene().getSprites()) {
            if (sprite.getId() == action.spriteId) {
                matchedSprite = sprite;
                break;
            }
        }

        if (matchedSprite == null) {
            return;
        }

//        if (action instanceof PlayerAction.NoAction) {
//            // Do nothing
//
//        } else if (action instanceof PlayerAction.DoMove) {
//            Direction direction = ((PlayerAction.DoMove) action).direction;
//            if (direction == null) {
//                Log.app().error("Null direction?!");
//            }
//            if (player.doMove(direction)) {
//                Log.app().trace("Client: Player " + action.spriteId + " DoMove " + direction.toString());
//            } else {
//                actionsRemaining.add(action);
//            }
//        } else if (action instanceof PlayerAction.SetBomb) {
//            if (player.setBomb()) {
//                Log.app().trace("Client: Player " + action.spriteId + " SetBomb");
//            } else {
//                actionsRemaining.add(action);
//            }
//        } else if (action instanceof PlayerAction.ExplodeBomb) {
//            if (player.explodeBomb()) {
//                Log.app().trace("Client: Player " + action.spriteId + " ExplodeBomb");
//            } else {
//                actionsRemaining.add(action);
//            }
//        } else {
//            Log.app().error(getClass().getName() + ": Null action or illegal/unsupported action!");
//        }
    }

    @Override
    protected void performAction(SceneAction action) {
        if (action instanceof SceneAction.AddSprites) {
            for (Sprite sprite : ((SceneAction.AddSprites) action).sprites) {
                app.getScene().getSceneUpdater().addSprite(sprite);
            }
        } else if (action instanceof SceneAction.RemoveSprites) {
            for (Sprite sprite : ((SceneAction.RemoveSprites) action).sprites) {
                app.getScene().getSceneUpdater().removeSpriteById(sprite.getId());
            }
        } else if (action instanceof SceneAction.UpdateSprites) {
            app.getScene().getSceneUpdater().updateSprites(((SceneAction.UpdateSprites) action).sprites);
//            app.getScene().getSprites().clear();
//            app.getScene().getSprites().addAll(((SceneAction.UpdateSprites) action).sprites);
        } else if (action instanceof SceneAction.AddThings) {
            for (Thing thing : ((SceneAction.AddThings) action).things) {
                app.getScene().getSceneUpdater().addThing(thing);
            }
        } else if (action instanceof SceneAction.RemoveThings) {
            for (Thing thing : ((SceneAction.RemoveThings) action).things) {
                app.getScene().getSceneUpdater().removeThingById(thing.getId());
            }
        } else if (action instanceof SceneAction.UpdateThings) {
            app.getScene().getSceneUpdater().updateThings(((SceneAction.UpdateThings) action).things);
        } else {
            Log.app().error(getClass().getName() + ": Null action or illegal/unsupported action!");
        }
    }

}
