package com.pandaawake.player;

import com.pandaawake.Config;
import com.pandaawake.scene.Scene;
import com.pandaawake.sprites.Bomb;
import com.pandaawake.sprites.MovableSprite;
import com.pandaawake.sprites.Snake;
import com.pandaawake.utils.Direction;
import com.pandaawake.utils.UtilFunctions;

import java.awt.event.KeyEvent;
import java.util.Random;

public class ComputerPlayer implements Player {
    private static final Direction[] directions = { Direction.left, Direction.up, Direction.right, Direction.down};

    private Snake snake;
    private final Random random;
    private final Scene scene;

    private float elapsedTime = 0.0f;
    private Direction direction;

    public ComputerPlayer(Scene scene, Snake snake, Direction defaultDirection) {
        random = new Random();
        this.scene = scene;
        this.snake = snake;
        this.direction = defaultDirection;
    }

    @Override
    public void OnUpdate(float timestep) {
        elapsedTime += timestep;
        if (elapsedTime >= Config.DecisionTime) {
            makeDecision();
            elapsedTime -= Config.DecisionTime;
        }
    }


    public void makeDecision() {
        float probability = UtilFunctions.getRandomProbability();
        if (probability <= Config.DoNothingProbability) {
            // Do nothing
        } else if (probability <= Config.DoNothingProbability + Config.SetBombProbability) {
            setBomb();
        } else {
            tryMove();
        }
    }

    public void tryMove() {
        if (UtilFunctions.getRandomResultByProbability(Config.ChangeDirectionProbability)) {
            direction = directions[random.nextInt(4)];
        }
        snake.doMove(direction);
    }

    @Override
    public void setBomb() {
        if (snake.getStatus() == MovableSprite.Status.Ok) {
            snake.setNewBomb();
        }
    }

    @Override
    public void OnKeyPressed(KeyEvent e) {

    }



    public static class ComputerPlayerThread extends Thread {
        private ComputerPlayer player;
        private long oldTime = 0;

        public ComputerPlayerThread(ComputerPlayer player) {
            this.player = player;
        }

        @Override
        public void run() {
            while (true) {
                if (oldTime == 0) {
                    oldTime = System.currentTimeMillis();
                }
                else {
                    long time = System.currentTimeMillis();
                    player.OnUpdate(time - oldTime);
                    oldTime = time;
                }
                try {
                    Thread.sleep(Math.round(1000.0f / Config.MaxFrameRate));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
