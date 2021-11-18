package com.pandaawake.player;

import com.mandas.tiled2d.core.Application;
import com.pandaawake.gourdgame.GameApp;
import com.pandaawake.scene.Scene;
import com.pandaawake.sprites.*;
import com.pandaawake.utils.Direction;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class HumanPlayer implements Player {

    private final Scene scene;
    private Calabash calabash;

    public HumanPlayer(Scene scene, Calabash calabash) {
        this.scene = scene;
        this.calabash = calabash;
    }

    public Calabash getCalabash() {
        return calabash;
    }

    public void tryMove() {

    }

    @Override
    public void setBomb() {
        if (calabash.getStatus() == MovableSprite.Status.Ok) {
            calabash.setNewBomb();
        }
    }

    @Override
    public void OnUpdate(float timestep) {
        if (!scene.getSprites().contains(calabash)) {
            // Game over
            GameApp.getGameApp().setPause(true);
            JOptionPane.showMessageDialog(null, "游戏结束！你输了！");
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
                GameApp.getGameApp().setPause(true);
                JOptionPane.showMessageDialog(null, "游戏结束！恭喜你赢了！");
            }
        }
    }

    @Override
    public void OnKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                calabash.doMove(Direction.left);
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                calabash.doMove(Direction.up);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                calabash.doMove(Direction.right);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                calabash.doMove(Direction.down);
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_NUMPAD0:
                for (Bomb bomb : calabash.getBombs()) {
                    bomb.setExplodeImmediately();
                }
                break;
            case KeyEvent.VK_J:
            case KeyEvent.VK_NUMPAD1:
                setBomb();
                break;
        }
    }
}
