package com.pandaawake.gourdgame.main;

import com.mandas.tiled2d.core.Log;
import com.mandas.tiled2d.event.EventDispatcher;
import com.mandas.tiled2d.event.KeyCodes;
import com.mandas.tiled2d.event.KeyEvents;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.network.GameClient;
import com.pandaawake.gourdgame.network.data.action.ConnectionAction;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;
import com.pandaawake.gourdgame.player.OtherPlayer;
import com.pandaawake.gourdgame.player.Player;
import com.pandaawake.gourdgame.player.Replayer;
import com.pandaawake.gourdgame.render.RenderCommand;
import com.pandaawake.gourdgame.scene.GameMap;
import com.pandaawake.gourdgame.scene.Level;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.scene.SceneTilesInitializer;
import com.pandaawake.gourdgame.scene.updater.ClientSceneUpdater;
import com.pandaawake.gourdgame.sprites.Snake;
import com.pandaawake.gourdgame.sprites.Sprite;
import com.pandaawake.gourdgame.utils.Direction;

import java.util.HashSet;
import java.util.Set;

public class ClientGameApp extends GameApp {

    protected GameMap gameMap;
    protected Scene scene;
    protected Level level;
    protected SceneTilesInitializer sceneTilesInitializer;
    protected Set<Player> players;
    protected OtherPlayer mainPlayer;
    protected int mainPlayerId = -1;

    protected Replayer replayer = null;

    protected GameClient gameClient;

    public boolean pause = true;
    public void setPause(boolean pause) {
        this.pause = pause;
    }
    public boolean getPause() {
        return pause;
    }

    public Scene getScene() {
        return scene;
    }

    public OtherPlayer getMainPlayer() {
        return mainPlayer;
    }

    public void setMainPlayer(OtherPlayer mainPlayer) {
        this.mainPlayer = mainPlayer;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public int getMainPlayerId() {
        return mainPlayerId;
    }

    public void setMainPlayerId(int mainPlayerId) {
        this.mainPlayerId = mainPlayerId;
    }


    public ClientGameApp() {
        gameMap = new GameMap(Config.MapWidth, Config.MapHeight);
        scene = new Scene(gameMap, new ClientSceneUpdater());
        level = new Level(Config.level1TileMap, null, null);
        sceneTilesInitializer = new SceneTilesInitializer(scene);
        players = new HashSet<>();
        if (Config.ReplayMode) {
            replayer = new Replayer(this);
        }
        gameClient = new GameClient(this);

        initializeEventDispatcher();
        initializeMapTileAndLevel();
        initializeClient();
    }

    protected void initializeEventDispatcher() {
        EventDispatcher.register(KeyEvents.Pressed.class, e -> OnPlayerKeyPressed(e));
    }

    protected void initializeMapTileAndLevel() {
        // ------ Initialize tiles ------
        sceneTilesInitializer.initializeTiles(level);
        scene.OnUpdate(0.0f);
    }

    protected void initializeClient() {
        new Thread(new GameClient.GameClientRunnable(gameClient)).start();
        gameClient.sendAction(new ConnectionAction.ClientEnter(-1));
    }


    public void resetAll() {
        RenderCommand.clear();
        gameMap.resetAll();
        scene.resetAll();

        // Reset players
        players.clear();

        mainPlayer = null;

        initializeMapTileAndLevel();
    }

    protected void checkGameEnds() {
        if (mainPlayer == null) {
            return;
        }
        if (!scene.getSprites().contains(mainPlayer.sprite)) {
            // Game over
            //setPause(true);
            //JOptionPane.showMessageDialog(null, "游戏结束！你输了！");
        } else {
            boolean NoSnakes = true;
            for (Sprite sprite : scene.getSprites()) {
                if (sprite instanceof Snake) {
                    NoSnakes = false;
                    break;
                }
            }
            if (NoSnakes) {
                // Game over
                //setPause(true);
                //JOptionPane.showMessageDialog(null, "游戏结束！恭喜你赢了！");
            }
        }
    }

    // ---------------------- GameApplication Functions ----------------------
    @Override
    public void InitRenderer() {
        RenderCommand.Init(scene);
    }

    @Override
    public void OnRender() {
        scene.OnRender();

        // Paint something on scoreboard
        RenderCommand.clearScoreboard();

        if (mainPlayer != null) {
            int playerLives = mainPlayer.sprite.getLives();
            RenderCommand.drawScoreboardString(25, 250, "HP: " + String.valueOf(playerLives));
            for (int i = 0; i < playerLives; i++) {
                RenderCommand.drawScoreboardTile(Config.TileSize * i, 150,
                        mainPlayer.sprite.getTileTextureRenderComponent().getPositionsAndTextures().get(0).second);
            }
        }
    }

    @Override
    public void OnUpdate(float timestep) {
        if (pause) {
            return;
        }

        checkGameEnds();

        scene.OnUpdate(timestep);
        for (Player player : players) {
            player.OnUpdate(timestep);
        }

        if (replayer != null) {
            replayer.OnUpdate(timestep);
        }
    }

    @Override
    public void OnDestroy() {

    }


    public void OnPlayerKeyPressed(KeyEvents.Pressed e) {
        if (pause) {
            return;
        }
        if (Config.ReplayMode) {
            return;
        }
        if (mainPlayer == null) {
            return;
        }
        switch (e.getKeyCode()) {
            case KeyCodes.VC_A:
            case KeyCodes.VC_LEFT:
                gameClient.sendAction(new PlayerAction.DoMove(-1, mainPlayerId, Direction.left));
                break;
            case KeyCodes.VC_W:
            case KeyCodes.VC_UP:
                gameClient.sendAction(new PlayerAction.DoMove(-1, mainPlayerId, Direction.up));
                break;
            case KeyCodes.VC_D:
            case KeyCodes.VC_RIGHT:
                gameClient.sendAction(new PlayerAction.DoMove(-1, mainPlayerId, Direction.right));
                break;
            case KeyCodes.VC_S:
            case KeyCodes.VC_DOWN:
                gameClient.sendAction(new PlayerAction.DoMove(-1, mainPlayerId, Direction.down));
                break;
            case KeyCodes.VC_SPACE:
            case KeyCodes.VC_0:
                gameClient.sendAction(new PlayerAction.ExplodeBomb(-1, mainPlayerId));
                break;
            case KeyCodes.VC_J:
            case KeyCodes.VC_1:
                gameClient.sendAction(new PlayerAction.SetBomb(-1, mainPlayerId));
                break;
        }
    }

}
