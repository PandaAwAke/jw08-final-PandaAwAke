package com.pandaawake.gourdgame.network.data.performer;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.main.ReplayerApp;
import com.pandaawake.gourdgame.network.data.action.ConnectionAction;
import com.pandaawake.gourdgame.network.data.action.GameAction;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;
import com.pandaawake.gourdgame.network.data.action.SceneAction;
import com.pandaawake.gourdgame.player.HumanPlayer;
import com.pandaawake.gourdgame.sprites.Bomb;
import com.pandaawake.gourdgame.sprites.Calabash;
import com.pandaawake.gourdgame.sprites.PlayableSprite;
import com.pandaawake.gourdgame.sprites.Sprite;
import com.pandaawake.gourdgame.utils.Direction;

import java.util.ArrayList;
import java.util.List;

public class ReplayerActionPerformer extends ActionPerformer {

    private ReplayerApp app;

    public ReplayerActionPerformer(ReplayerApp app) {
        this.app = app;
    }


    @Override
    protected void performAction(GameAction action) {
        if (action instanceof GameAction.GameStart) {
            app.setPause(false);
        } else if (action instanceof GameAction.GamePause) {
            app.setPause(true);
        } else if (action instanceof GameAction.GameResume) {
            app.setPause(false);
        } else if (action instanceof GameAction.GameEnd) {
            app.setPause(true);
            // TODO: Other things?
        } else if (action instanceof GameAction.GameInitialize) {
            GameAction.GameInitialize initAction = (GameAction.GameInitialize) action;
            String playerName = initAction.playerName;
            Calabash humanCalabash = new Calabash(initAction.spriteId, app.getScene());
            humanCalabash.setPos(Config.level.humanPlayerPositions.get(initAction.senderClientId).first,
                    Config.level.humanPlayerPositions.get(initAction.senderClientId).second);
            app.getScene().getSceneUpdater().addSprite(humanCalabash);
            app.getPlayers().add(new HumanPlayer(humanCalabash, initAction.senderClientId, playerName));
        }
    }

    @Override
    protected void performAction(ConnectionAction action) {

    }

    @Override
    protected void performAction(PlayerAction action) {
        List<PlayableSprite> matchedSprites = new ArrayList<>();
        for (Sprite sprite : app.getScene().getSprites()) {
            if (sprite.getId() == action.spriteId && sprite instanceof PlayableSprite) {
                matchedSprites.add((PlayableSprite) sprite);
            }
        }

        if (matchedSprites.size() != 1) {
            Log.app().error("No matched sprite?");
            return;
        }

        PlayableSprite matchedSprite = matchedSprites.get(0);

        if (action instanceof PlayerAction.NoAction) {
            // Do nothing
        } else if (action instanceof PlayerAction.DoMove) {
            Direction direction = ((PlayerAction.DoMove) action).direction;
            if (direction == null) {
                Log.app().error("Null direction?!");
            }
            if (matchedSprite.doMove(direction)) {
                Log.app().trace("Sprite " + matchedSprite.getId() + " DoMove " + direction.toString());
            }
        } else if (action instanceof PlayerAction.SetBomb) {
            if (matchedSprite.canSetBomb()) {
                matchedSprite.setNewBomb();
                Log.app().trace("Sprite " + matchedSprite.getId() + " SetBomb");
            }
        } else if (action instanceof PlayerAction.ExplodeBomb) {
            Log.app().trace("Sprite " + matchedSprite.getId() + " ExplodeBomb");
            for (Bomb bomb : matchedSprite.getBombs()) {
                bomb.setExplodeImmediately();
            }
        } else {
            Log.app().error(getClass().getName() + ": Null action or illegal/unsupported action!");
        }
    }

    @Override
    protected void performAction(SceneAction action) {

    }

}
