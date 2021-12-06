package com.pandaawake.gourdgame.player;

import com.mandas.tiled2d.event.KeyCodes;
import com.mandas.tiled2d.event.KeyEvents;
import com.pandaawake.gourdgame.main.GameApp;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.sprites.*;
import com.pandaawake.gourdgame.utils.Direction;

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
    public void OnKeyPressed(KeyEvents.Pressed e) {
        switch (e.getKeyCode()) {
            case KeyCodes.VC_A:
            case KeyCodes.VC_LEFT:
                calabash.doMove(Direction.left);
                break;
            case KeyCodes.VC_W:
            case KeyCodes.VC_UP:
                calabash.doMove(Direction.up);
                break;
            case KeyCodes.VC_D:
            case KeyCodes.VC_RIGHT:
                calabash.doMove(Direction.right);
                break;
            case KeyCodes.VC_S:
            case KeyCodes.VC_DOWN:
                calabash.doMove(Direction.down);
                break;
            case KeyCodes.VC_SPACE:
            case KeyCodes.VC_0:
                for (Bomb bomb : calabash.getBombs()) {
                    bomb.setExplodeImmediately();
                }
                break;
            case KeyCodes.VC_J:
            case KeyCodes.VC_1:
                setBomb();
                break;
        }
    }
}
