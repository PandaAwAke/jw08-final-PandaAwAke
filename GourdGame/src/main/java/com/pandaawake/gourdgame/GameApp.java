package com.pandaawake.gourdgame;

import com.pandaawake.Config;
import com.pandaawake.gamemap.GameMap;
import com.pandaawake.gamemap.Scene;
import com.pandaawake.renderer.RenderCommand;
import com.pandaawake.sprites.CalabashPlayer;
import com.pandaawake.utils.Direction;


import java.awt.event.KeyEvent;

public class GameApp {

    private static GameApp globalGameApp = null;
    public static GameApp getGameApp() {
        if (globalGameApp == null) {
            globalGameApp = new GameApp();
        }
        return globalGameApp;
    }

    private RenderCommand renderer = null;

    private GameMap gameMap;
    private Scene scene;
    private CalabashPlayer calabashPlayer;


    private GameApp() {
        gameMap = new GameMap(Config.MapWidth, Config.MapHeight);
        scene = new Scene(gameMap);
        calabashPlayer = new CalabashPlayer(scene);

        calabashPlayer.setX(1);
        calabashPlayer.setY(1);
        this.scene.addSprite(calabashPlayer);

    }

    /**
     * Renderer should be set before rendering
     * @param renderer
     */
    public void setRenderer(RenderCommand renderer) {
        this.renderer = renderer;
    }

    public Scene getScene() {
        return scene;
    }

    public void OnUpdate(float timestep) {
        // TODO: Update all
        scene.OnUpdate(timestep);
        
    }

    public void OnRender() {
        if (renderer == null) {
            throw new NullPointerException("Renderer is not set in GameApp!");
        }
        renderer.draw(scene);
    }

    public void OnKeyPressed(KeyEvent e) {
        Direction direction = null;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                direction = Direction.left;
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                direction = Direction.up;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                direction = Direction.right;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                direction = Direction.down;
                break;
        }

        if (direction != null) {
            calabashPlayer.doMove(direction);
        }
    }
    
}
