package com.pandaawake.gourdgame;

import com.pandaawake.Config;
import com.pandaawake.core.Application;
import com.pandaawake.scene.*;
import com.pandaawake.sprites.*;
import com.pandaawake.utils.Direction;
import com.pandaawake.utils.IntPair;


import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class GameApp {

    private static GameApp globalGameApp = null;
    public static GameApp getGameApp() {
        if (globalGameApp == null) {
            globalGameApp = new GameApp();
        }
        return globalGameApp;
    }


    private GameMap gameMap;
    private Scene scene;
    private SceneTilesInitializer sceneTilesInitializer;
    private Set<Player> players;

    private GameApp() {
        gameMap = new GameMap(Config.MapWidth, Config.MapHeight);
        scene = new Scene(gameMap);
        players = new HashSet<>();

        sceneTilesInitializer = new SceneTilesInitializer(scene);
        sceneTilesInitializer.initializeTiles();
        initializeSprites();
        initializeHumanPlayers();
        initializeComputerPlayers();
    }


    private void initializeHumanPlayers() {
        Calabash humanCalabash = new Calabash(scene);
        IntPair position = new IntPair(0, 1);
        //IntPair position = sceneTilesInitializer.getASpriteEntryPositionRandomly(humanCalabash);
        humanCalabash.setPos(position.first, position.second);
        scene.addSprite(humanCalabash);

        HumanPlayer humanPlayer = new HumanPlayer(scene, humanCalabash);
        players.add(humanPlayer);
    }

    private void initializeSprites() {

    }

    private void initializeComputerPlayers() {
        for (int i = 0; i < 3; i++) {
            Snake computerSnake = new Snake(scene);
            ComputerPlayer computerSnakePlayer;
            IntPair position;
            //IntPair position = sceneTilesInitializer.getASpriteEntryPositionRandomly(computerSnake);
            if (i == 0) {
                position = new IntPair(0, 13);
                computerSnakePlayer = new ComputerPlayer(scene, computerSnake, Direction.up);
            } else if (i == 1) {
                position = new IntPair(15, 13);
                computerSnakePlayer = new ComputerPlayer(scene, computerSnake, Direction.up);
            } else {
                position = new IntPair(15, 1);
                computerSnakePlayer = new ComputerPlayer(scene, computerSnake, Direction.down);
            }
            computerSnake.setPos(position.first, position.second);
            scene.addSprite(computerSnake);

            players.add(computerSnakePlayer);

            // Create threads and let's go!
            ComputerPlayer.ComputerPlayerThread thread = new ComputerPlayer.ComputerPlayerThread(computerSnakePlayer);
            thread.setDaemon(true);
            thread.start();
        }
    }



    // ---------------------- Callback Functions ----------------------
    public void OnUpdate(float timestep) {
        scene.OnUpdate(timestep);

        for (Player player : players) {
            player.OnUpdate(timestep);
        }
    }

    public void OnRender() {
        scene.OnRender();
    }

    public void OnKeyPressed(KeyEvent e) {
        for (Player player : players) {
            player.OnKeyPressed(e);
        }
    }
    
}
