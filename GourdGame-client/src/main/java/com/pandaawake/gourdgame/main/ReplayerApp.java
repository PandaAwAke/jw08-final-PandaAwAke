package com.pandaawake.gourdgame.main;

import com.mandas.tiled2d.utils.IntPair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.player.HumanPlayer;
import com.pandaawake.gourdgame.player.OtherPlayer;
import com.pandaawake.gourdgame.player.Player;
import com.pandaawake.gourdgame.player.Replayer;
import com.pandaawake.gourdgame.render.RenderCommand;
import com.pandaawake.gourdgame.scene.GameMap;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.scene.SceneTilesInitializer;
import com.pandaawake.gourdgame.scene.updater.SceneUpdater;
import com.pandaawake.gourdgame.sprites.Snake;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReplayerApp extends GameApp {

    protected GameMap gameMap;
    protected Scene scene;
    protected SceneTilesInitializer sceneTilesInitializer;
    protected Set<Player> players;

    protected HumanPlayer mainPlayer = null;

    protected Replayer replayer = null;


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

    public Set<Player> getPlayers() {
        return players;
    }


    public ReplayerApp() {
        gameMap = new GameMap(Config.MapWidth, Config.MapHeight);
        scene = new Scene(gameMap, new SceneUpdater());
        sceneTilesInitializer = new SceneTilesInitializer(scene);
        players = new HashSet<>();
        replayer = new Replayer(this);

        initializeMapTileAndLevel();
    }


    protected void initializeMapTileAndLevel() {
        // ------ Initialize tiles ------
        sceneTilesInitializer.initializeTiles(Config.level);

        // ------ Initialize computer player positions ------
        List<String> names = Config.level.computerPlayerNames;
        int index = 0;
        for (IntPair position : Config.level.computerPlayerPositions) {
            Snake computerSnake = new Snake(Scene.getNextSpriteId(), scene);
            OtherPlayer computerSnakePlayer = new OtherPlayer(computerSnake, index + 100, names.get(index++));
            computerSnake.setPos(position.first, position.second);
            scene.getSceneUpdater().addSprite(computerSnake);

            players.add(computerSnakePlayer);
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

        replayer.OnUpdate(timestep);

        mainPlayer = null;
        for (Player player : players) {
            if (player instanceof HumanPlayer) {
                mainPlayer = (HumanPlayer) player;
                mainPlayer.sprite.getCameraComponent().setRenderingCamera(true);
                break;
            }
        }

        scene.OnUpdate(timestep);
        scene.setEntities(scene.getSprites());
        scene.getSceneUpdater().OnUpdate(timestep);
    }

    @Override
    public void OnDestroy() {
        replayer.OnDestroy();
    }



}
