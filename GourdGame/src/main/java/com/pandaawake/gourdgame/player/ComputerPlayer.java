package com.pandaawake.gourdgame.player;

import com.mandas.tiled2d.core.Log;
import com.mandas.tiled2d.event.KeyEvents;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.sprites.MovableSprite;
import com.pandaawake.gourdgame.sprites.Snake;
import com.pandaawake.gourdgame.utils.Direction;
import com.pandaawake.gourdgame.utils.UtilFunctions;

import java.util.Random;

public class ComputerPlayer implements Player {
    private static final Direction[] directions = { Direction.left, Direction.up, Direction.right, Direction.down};

    private Snake snake;
    private final Random random;
    private final Scene scene;

    private float elapsedTime = 0.0f;
    private Direction direction;

    private String name;

    private boolean running = true;

    public ComputerPlayer(Scene scene, Snake snake, Direction defaultDirection, String name) {
        random = new Random();
        this.scene = scene;
        this.snake = snake;
        this.direction = defaultDirection;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setRunning(boolean running) {
        synchronized (this) {
            this.running = running;
        }
    }

    public boolean getRunning() {
        synchronized (this) {
            return running;
        }
    }

    @Override
    public void OnUpdate(float timestep) {
        if (Config.ReplayMode) {
            return;
        }
        elapsedTime += timestep;
        if (elapsedTime >= Config.DecisionTime) {
            makeDecision();
            elapsedTime = 0.0f;
        }
    }

    public void makeDecision() {
        float probability = UtilFunctions.getRandomProbability();
        if (probability <= Config.DoNothingProbability) {
            // Do nothing
            Log.file().trace("Computer " + this.name + " NoAction");
        } else if (probability <= Config.DoNothingProbability + Config.SetBombProbability) {
            Log.file().trace("Computer " + this.name + " SetBomb");
            setBomb();
        } else {
            tryMove();
        }
    }

    public void tryMove() {
        if (UtilFunctions.getRandomResultByProbability(Config.ChangeDirectionProbability)) {
            direction = directions[random.nextInt(4)];
        }
        Log.file().trace("Computer " + this.name + " DoMove " + direction.toString());
        doMove(direction);
    }



    @Override
    public void doMove(Direction direction) {
        snake.doMove(direction);
    }

    @Override
    public void setBomb() {
        snake.setNewBomb();
    }

    @Override
    public void explodeBomb() {

    }

    @Override
    public void OnKeyPressed(KeyEvents.Pressed e) {

    }

}
