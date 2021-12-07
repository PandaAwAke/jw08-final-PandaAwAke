package com.pandaawake.gourdgame.player;

import com.mandas.tiled2d.core.Log;
import com.mandas.tiled2d.event.KeyCodes;
import com.mandas.tiled2d.event.KeyEvents;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.main.GameApp;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.sprites.*;
import com.pandaawake.gourdgame.utils.Direction;

import javax.swing.*;

public class HumanPlayer implements Player {

    private final Scene scene;
    private Calabash calabash;
    private String name;

    public HumanPlayer(Scene scene, Calabash calabash, String name) {
        this.scene = scene;
        this.calabash = calabash;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public Calabash getCalabash() {
        return calabash;
    }




    @Override
    public void OnUpdate(float timestep) {
        if (Config.ReplayMode) {
            return;
        }

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
        if (GameApp.getGameApp().getPause()) {
            return;
        }
        if (Config.ReplayMode) {
            return;
        }
        switch (e.getKeyCode()) {
            case KeyCodes.VC_A:
            case KeyCodes.VC_LEFT:
                Log.file().trace("Human " + this.name + " DoMove left");
                calabash.doMove(Direction.left);
                break;
            case KeyCodes.VC_W:
            case KeyCodes.VC_UP:
                Log.file().trace("Human " + this.name + " DoMove up");
                calabash.doMove(Direction.up);
                break;
            case KeyCodes.VC_D:
            case KeyCodes.VC_RIGHT:
                Log.file().trace("Human " + this.name + " DoMove right");
                calabash.doMove(Direction.right);
                break;
            case KeyCodes.VC_S:
            case KeyCodes.VC_DOWN:
                Log.file().trace("Human " + this.name + " DoMove down");
                calabash.doMove(Direction.down);
                break;
            case KeyCodes.VC_SPACE:
            case KeyCodes.VC_0:
                Log.file().trace("Human " + this.name + " ExplodeBomb");
                explodeBomb();
                break;
            case KeyCodes.VC_J:
            case KeyCodes.VC_1:
                Log.file().trace("Human " + this.name + " SetBomb");
                setBomb();
                break;
        }
    }



    @Override
    public void doMove(Direction direction) {
        calabash.doMove(direction);
    }

    @Override
    public void setBomb() {
        calabash.setNewBomb();
    }

    @Override
    public void explodeBomb() {
        for (Bomb bomb : calabash.getBombs()) {
            bomb.setExplodeImmediately();
        }
    }
}
