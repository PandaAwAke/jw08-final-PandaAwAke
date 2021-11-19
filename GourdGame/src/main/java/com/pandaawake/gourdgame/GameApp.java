package com.pandaawake.gourdgame;

import com.mandas.tiled2d.core.GameApplication;
import com.pandaawake.Config;
import com.pandaawake.player.ComputerPlayer;
import com.pandaawake.player.HumanPlayer;
import com.pandaawake.player.Player;
import com.pandaawake.render.RenderCommand;
import com.pandaawake.scene.*;
import com.pandaawake.sprites.*;
import com.pandaawake.utils.Direction;
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

        initializeSprites();
        initializeMapTileAndLevel();
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

            // Create threads and let's go!
            ComputerPlayer.ComputerPlayerThread thread = new ComputerPlayer.ComputerPlayerThread(computerSnakePlayer);
            thread.setDaemon(true);
            thread.start();
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
        RenderCommand.Init();
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
                    mainPlayer.getCalabash().getTextures().get(0));
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

    @Override
    public void OnKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setPause(!getPause());
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_F5) {
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
    }

}
