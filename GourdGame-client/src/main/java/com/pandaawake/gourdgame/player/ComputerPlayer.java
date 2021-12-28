package com.pandaawake.gourdgame.player;

import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.network.GameServer;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;
import com.pandaawake.gourdgame.sprites.Snake;
import com.pandaawake.gourdgame.utils.Direction;
import com.pandaawake.gourdgame.utils.RandomUtils;

import java.util.Random;

public class ComputerPlayer extends Player {
    private static final Direction[] directions = { Direction.left, Direction.up, Direction.right, Direction.down};

    private final Random random;

    private float elapsedTime = 0.0f;
    private Direction direction;

    private GameServer gameServer;

    public ComputerPlayer(GameServer gameServer, Snake snake, Direction defaultDirection, int id, String name) {
        super(snake, id, name);
        this.gameServer = gameServer;
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
        } else if (probability <= Config.DoNothingProbability + Config.SetBombProbability) {
            gameServer.getActionPerformer().performAction(new PlayerAction.SetBomb(-1, sprite.getId()));
        } else {
            tryMove();
        }
    }

    public void tryMove() {
        if (RandomUtils.getRandomResultByProbability(Config.ChangeDirectionProbability)) {
            direction = directions[random.nextInt(4)];
        }
        gameServer.getActionPerformer().performAction(new PlayerAction.DoMove(-1, sprite.getId(), direction));
    }

    @Override
    public boolean explodeBomb() {
        return false;
    }

}
