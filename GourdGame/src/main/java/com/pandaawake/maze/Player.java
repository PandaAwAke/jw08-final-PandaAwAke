package com.pandaawake.maze;

import com.pandaawake.calabashbros.Creature;
import com.pandaawake.calabashbros.Thing;
import com.pandaawake.calabashbros.World;

public class Player {

    public enum Direction {
        left, up, right, down
    }

    private Maze maze;
    private int x, y;
    private Creature creature;
    private World world;

    public Player(Maze maze, World world, int playerX, int playerY, Creature creature) {
        this.maze = maze;
        this.world = world;
        this.x = playerX;
        this.y = playerY;
        this.creature = creature;

        assert maze.insideMaze(x, y);
    }

    public boolean canMove(Direction direction) {
        int i = x, j = y;
        switch (direction) {
        case left:
            j -= 1;
            break;
        case up:
            i -= 1;
            break;
        case right:
            j += 1;
            break;
        case down:
            i += 1;
            break;
        }
        return canStay(i, j);
    }

    public boolean doMove(Direction direction) {
        if (!canMove(direction)) {
            return false;
        }
        int i = x, j = y;
        switch (direction) {
        case left:
            y -= 1;
            break;
        case up:
            x -= 1;
            break;
        case right:
            y += 1;
            break;
        case down:
            x += 1;
            break;
        }
        Thing thing = world.get(x, y);
        world.put(creature, x, y);
        world.put(thing, i, j);

        return true;
    }

    private boolean canStay(int i, int j) {
        return maze.insideMaze(i, j) && maze.isFloor(i, j);
    }

    public int getPlayerX() {
        return x;
    }

    public int getPlayerY() {
        return y;
    }

}
