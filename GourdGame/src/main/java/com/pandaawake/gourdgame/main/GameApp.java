package com.pandaawake.gourdgame.main;

import com.mandas.tiled2d.core.GameApplication;
import com.mandas.tiled2d.event.EventDispatcher;
import com.mandas.tiled2d.event.KeyCodes;
import com.mandas.tiled2d.event.KeyEvents;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.player.ComputerPlayer;
import com.pandaawake.gourdgame.player.HumanPlayer;
import com.pandaawake.gourdgame.player.Player;
import com.pandaawake.gourdgame.render.RenderCommand;
import com.pandaawake.gourdgame.scene.*;
import com.pandaawake.gourdgame.sprites.*;
import com.pandaawake.gourdgame.utils.Direction;
import com.mandas.tiled2d.utils.IntPair;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class GameApp implements GameApplication {

    private static GameApp globalGameApp = null;

    public static GameApp getGameApp() {
        if (globalGameApp == null) {
            globalGameApp = new GameApp();
        }
        return globalGameApp;
    }

    private GameMap gameMap;
    private Scene scene;
    private Level level;
    private SceneTilesInitializer sceneTilesInitializer;
    private Set<Player> players;
    private HumanPlayer mainPlayer;


    public boolean pause = false;
    public void setPause(boolean pause) {
        this.pause = pause;
    }
    public boolean getPause() {
        return pause;
    }

    private GameApp() {
        gameMap = new GameMap(Config.MapWidth, Config.MapHeight);
        scene = new Scene(gameMap);
        level = new Level(Config.level1TileMap, Config.level1HumanPlayerPositions, Config.level1ComputerPlayerPositions);
        sceneTilesInitializer = new SceneTilesInitializer(scene);
        players = new HashSet<>();

        initializeEventDispatcher();
        initializeSprites();
        initializeMapTileAndLevel();
    }

    private void initializeEventDispatcher() {
        EventDispatcher.register(KeyEvents.Pressed.class, e -> {
            if (e.getKeyCode() == KeyCodes.VC_ESCAPE) {
                setPause(!getPause());
                return;
            }
            if (e.getKeyCode() == KeyCodes.VC_F5) {
                resetAll();
                pause = false;
                return;
            }
            if (pause) {
                return;
            }
            for (Player player : players) {
                player.OnKeyPressed(e);
            }
        });
    }

    private void initializeSprites() {

    }

    private void initializeMapTileAndLevel() {
        // ------ Initialize tiles ------
        sceneTilesInitializer.initializeTiles(level);

        // ------ Initialize human player positions ------
        for (IntPair position : level.humanPlayerPositions) {
            Calabash humanCalabash = new Calabash(scene);
            humanCalabash.setPos(position.first, position.second);
            scene.addSprite(humanCalabash);

            // TODO: mainPlayer and multi-players?
            mainPlayer = new HumanPlayer(scene, humanCalabash);
            players.add(mainPlayer);
        }

        // ------ Initialize computer player positions ------
        for (IntPair position : level.computerPlayerPositions) {
            Snake computerSnake = new Snake(scene);
            ComputerPlayer computerSnakePlayer = new ComputerPlayer(scene, computerSnake, Direction.down);
            computerSnake.setPos(position.first, position.second);
            scene.addSprite(computerSnake);

            players.add(computerSnakePlayer);
        }
    }

    public void resetAll() {
        RenderCommand.clear();
        gameMap.resetAll();
        scene.resetAll();

        // Reset players
        for (Player player : players) {
            if (player instanceof ComputerPlayer) {
                ((ComputerPlayer) player).setRunning(false);
            }
        }
        players.clear();

        initializeSprites();
        initializeMapTileAndLevel();
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

        int playerLives = mainPlayer.getCalabash().getLives();
        RenderCommand.drawScoreboardString(25, 250, "HP: " + String.valueOf(playerLives));
        for (int i = 0; i < playerLives; i++) {
            RenderCommand.drawScoreboardTile(0 + Config.TileSize * i, 150,
                    mainPlayer.getCalabash().getTileTextureRenderComponent().getPositionsAndTextures().get(0).second);
        }
    }

    @Override
    public void OnUpdate(float timestep) {
        if (pause) {
            return;
        }
        scene.OnUpdate(timestep);
        for (Player player : players) {
            player.OnUpdate(timestep);
        }
    }

}
