package com.pandaawake.gourdgame.main;

import com.mandas.tiled2d.event.EventDispatcher;
import com.mandas.tiled2d.event.KeyCodes;
import com.mandas.tiled2d.event.KeyEvents;
import com.mandas.tiled2d.utils.IntPair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.network.GameServer;
import com.pandaawake.gourdgame.network.data.action.GameAction;
import com.pandaawake.gourdgame.player.ComputerPlayer;
import com.pandaawake.gourdgame.player.HumanPlayer;
import com.pandaawake.gourdgame.player.Player;
import com.pandaawake.gourdgame.render.RenderCommand;
import com.pandaawake.gourdgame.scene.GameMap;
import com.pandaawake.gourdgame.scene.Level;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.scene.SceneTilesInitializer;
import com.pandaawake.gourdgame.scene.updater.ServerSceneUpdater;
import com.pandaawake.gourdgame.sprites.Snake;
import com.pandaawake.gourdgame.utils.Direction;

import java.util.HashSet;
import java.util.Set;

public class ServerGameApp {

    protected GameMap gameMap;
    protected Scene scene;
    protected Level level;
    protected SceneTilesInitializer sceneTilesInitializer;
    protected Set<Player> players;

    protected GameServer gameServer;

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

    public Player getPlayerById(int playerId) {
        for (Player player : players) {
            if (player.id == playerId) {
                return player;
            }
        }
        return null;
    }


    public ServerGameApp() {
        gameMap = new GameMap(Config.MapWidth, Config.MapHeight);
        scene = new Scene(gameMap, new ServerSceneUpdater());
        level = new Level(Config.level1TileMap, Config.level1HumanPlayerPositions, Config.level1ComputerPlayerPositions);
        sceneTilesInitializer = new SceneTilesInitializer(scene);
        players = new HashSet<>();
        gameServer = new GameServer(this);

        initializeEventDispatcher();
        initializeMapTileAndLevel();
        initializeServer();
    }

    protected void initializeEventDispatcher() {
        EventDispatcher.register(KeyEvents.Pressed.class, e -> {
            if (e.getKeyCode() == KeyCodes.VC_ESCAPE) {
                if (pause) {
                    pause = false;
                    gameServer.sendAction(new GameAction.GameResume(-1));
                } else {
                    pause = true;
                    gameServer.sendAction(new GameAction.GamePause(-1));
                }
            } else if (e.getKeyCode() == KeyCodes.VC_F5) {
//                resetAll();
//                pause = false;
//                return;
            } else if (e.getKeyCode() == KeyCodes.VC_F1) {
                pause = false;
                gameServer.sendAction(new GameAction.GameInitialize(-1, players));
                gameServer.sendAction(new GameAction.GameStart(-1));
            } else if (e.getKeyCode() == KeyCodes.VC_F2) {
                pause = true;
                gameServer.sendAction(new GameAction.GameEnd(-1));
            }
            if (pause) {
                return;
            }
        });
    }

    protected void initializeMapTileAndLevel() {
        // ------ Initialize tiles ------
        sceneTilesInitializer.initializeTiles(level);

        // ------ Initialize computer player positions ------
        String[] names = {"Alice", "Bob", "Tom", "Jerry"};
        int index = 0;
        for (IntPair position : level.computerPlayerPositions) {
            Snake computerSnake = new Snake(scene);
            ComputerPlayer computerSnakePlayer = new ComputerPlayer(gameServer, computerSnake, Direction.down, index + 100, names[index++]);
            computerSnake.setPos(position.first, position.second);
            scene.getSceneUpdater().addSprite(computerSnake);

            players.add(computerSnakePlayer);
        }
    }

    protected void initializeServer() {
        new Thread(new GameServer.GameServerRunnable(gameServer)).start();
    }

    public void resetAll() {
        RenderCommand.clear();
        gameMap.resetAll();
        scene.resetAll();

        // Reset players
        players.clear();

        initializeMapTileAndLevel();
    }

    protected void checkGameEnds() {
        boolean noHumanPlayers = true;
        boolean noComputerPlayers = true;
        for (Player player : players) {
            if (player instanceof HumanPlayer) {
                noHumanPlayers = false;
            } else {
                noComputerPlayers = false;
            }
        }

        if (noHumanPlayers) {
            // Game over
            pause = true;

        } else if (noComputerPlayers) {
            // Game over
            pause = true;

        }
    }

    public void OnUpdate(float timestep) {
        if (pause) {
            return;
        }

        checkGameEnds();

        scene.OnUpdate(timestep);
        
        for (Player player : players) {
            player.OnUpdate(timestep);
        }

    }

    public void OnDestroy() {
        gameServer.close();
    }

}
