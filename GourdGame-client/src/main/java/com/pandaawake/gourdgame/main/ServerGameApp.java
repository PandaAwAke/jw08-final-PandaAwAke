package com.pandaawake.gourdgame.main;

import com.mandas.tiled2d.core.Log;
import com.mandas.tiled2d.event.EventDispatcher;
import com.mandas.tiled2d.event.KeyCodes;
import com.mandas.tiled2d.event.KeyEvents;
import com.mandas.tiled2d.utils.IntPair;
import com.mandas.tiled2d.utils.Pair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.network.GameServer;
import com.pandaawake.gourdgame.network.data.action.GameAction;
import com.pandaawake.gourdgame.player.ComputerPlayer;
import com.pandaawake.gourdgame.player.Player;
import com.pandaawake.gourdgame.scene.GameMap;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.scene.SceneTilesInitializer;
import com.pandaawake.gourdgame.scene.updater.ServerSceneUpdater;
import com.pandaawake.gourdgame.sprites.Calabash;
import com.pandaawake.gourdgame.sprites.Snake;
import com.pandaawake.gourdgame.sprites.Sprite;
import com.pandaawake.gourdgame.utils.Direction;

import java.util.*;

public class ServerGameApp {

    protected GameMap gameMap;
    protected Scene scene;
    protected SceneTilesInitializer sceneTilesInitializer;
    protected Set<Player> players;

    protected Map<Integer, Pair<Integer, String>> clientIdsAndSpriteIdsNames;

    public Map<Integer, Pair<Integer, String>> getClientIdsAndSpriteIdsNames() {
        return clientIdsAndSpriteIdsNames;
    }

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
        gameServer = new GameServer(this);
        scene = new Scene(gameMap, new ServerSceneUpdater(gameServer));
        sceneTilesInitializer = new SceneTilesInitializer(scene);
        clientIdsAndSpriteIdsNames = new HashMap<>();
        players = new HashSet<>();


        initializeEventDispatcher();
        initializeMapTileAndLevel();
        initializeServer();
    }

    protected void initializeEventDispatcher() {
        EventDispatcher.register(KeyEvents.Pressed.class, e -> {
            if (e.getKeyCode() == KeyCodes.VK_ESCAPE) {
                if (pause) {
                    pause = false;
                    gameServer.sendAction(new GameAction.GameResume(-1));
                } else {
                    pause = true;
                    gameServer.sendAction(new GameAction.GamePause(-1));
                }
            } else if (e.getKeyCode() == KeyCodes.VK_F5) {
//                resetAll();
//                pause = false;
//                return;
            } else if (e.getKeyCode() == KeyCodes.VK_F1) {
                pause = false;
                for (int clientId : clientIdsAndSpriteIdsNames.keySet()) {
                    gameServer.sendAction(
                            new GameAction.GameInitialize(-1, clientIdsAndSpriteIdsNames.get(clientId).first, clientIdsAndSpriteIdsNames.get(clientId).second),
                            clientId);
                }
                gameServer.sendAction(new GameAction.GameStart(-1));
                Log.file().trace("Game Started!");
            }
//            else if (e.getKeyCode() == KeyCodes.VK_F2) {
//                pause = true;
//                gameServer.sendAction(new GameAction.GameEnd(-1));
//            }
            if (pause) {
                return;
            }
        });
    }

    protected void initializeMapTileAndLevel() {
        // ------ Initialize tiles ------
        sceneTilesInitializer.initializeTiles(Config.level);

        // ------ Initialize computer player positions ------
        List<String> names = Config.level.computerPlayerNames;
        int index = 0;
        for (IntPair position : Config.level.computerPlayerPositions) {
            Snake computerSnake = new Snake(Scene.getNextSpriteId(), scene);
            ComputerPlayer computerSnakePlayer = new ComputerPlayer(gameServer, computerSnake, Direction.down, index + 100, names.get(index++));
            computerSnake.setPos(position.first, position.second);
            scene.getSceneUpdater().addSprite(computerSnake);

            players.add(computerSnakePlayer);
        }
    }

    protected void initializeServer() {
        new Thread(new GameServer.GameServerRunnable(gameServer)).start();
    }

    public void resetAll() {
        gameMap.resetAll();
        scene.resetAll();

        // Reset players
        players.clear();

        initializeMapTileAndLevel();
    }

    protected void checkGameEnds() {
        boolean noHumanPlayers = true;
        boolean noComputerPlayers = true;
        if (scene.getSprites().size() == 0) {
            return;
        }
        for (Sprite sprite : scene.getSprites()) {
            if (sprite instanceof Calabash) {
                noHumanPlayers = false;
            } else if (sprite instanceof Snake) {
                noComputerPlayers = false;
            }
        }

        if (noHumanPlayers) {
            // Game over
            gameServer.sendAction(new GameAction.GameEnd(-1, false));
            pause = true;

        } else if (noComputerPlayers) {
            // Game over
            gameServer.sendAction(new GameAction.GameEnd(-1, true));
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
