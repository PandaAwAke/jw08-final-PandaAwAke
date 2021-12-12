package com.pandaawake.gourdgame.player;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.sprites.Snake;
import com.pandaawake.gourdgame.utils.Direction;
import com.pandaawake.gourdgame.utils.RandomUtils;

import java.util.Random;

public class ComputerPlayer extends Player {
    private static final Direction[] directions = { Direction.left, Direction.up, Direction.right, Direction.down};

    private final Random random;

    private float elapsedTime = 0.0f;
    private Direction direction;

    public ComputerPlayer(Snake snake, Direction defaultDirection, int id, String name) {
        super(snake, id, name);
        random = new Random();
        this.direction = defaultDirection;
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
        float probability = RandomUtils.getRandomProbability();
        if (probability <= Config.DoNothingProbability) {
            // Do nothing
            Log.file().trace(this.name + " NoAction");
        } else if (probability <= Config.DoNothingProbability + Config.SetBombProbability) {
            Log.file().trace(this.name + " SetBomb");
            setBomb();
        } else {
            tryMove();
        }
    }

    public void tryMove() {
        if (RandomUtils.getRandomResultByProbability(Config.ChangeDirectionProbability)) {
            direction = directions[random.nextInt(4)];
        }
        Log.file().trace(this.name + " DoMove " + direction.toString());
        doMove(direction);
    }



    @Override
    public void doMove(Direction direction) {
        sprite.doMove(direction);
    }

    @Override
    public void setBomb() {
        sprite.setNewBomb();
    }

    @Override
    public void explodeBomb() {

    }

}
