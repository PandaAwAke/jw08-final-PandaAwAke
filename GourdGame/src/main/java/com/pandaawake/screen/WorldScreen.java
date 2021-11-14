package com.pandaawake.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.pandaawake.calabashbros.Calabash;
import com.pandaawake.calabashbros.Floor;
import com.pandaawake.calabashbros.Wall;
import com.pandaawake.calabashbros.World;
import com.pandaawake.maze.Maze;
import com.pandaawake.maze.Node;
import com.pandaawake.maze.Player;
import com.pandaawake.maze.Player.Direction;

import asciiPanel.AsciiPanel;

public class WorldScreen implements Screen {

    private World world;
    private Maze maze;
    private Player player;

    private int dimension;

    public WorldScreen(int dimension) {
        this.dimension = dimension;
        world = new World(dimension, dimension);

        maze = new Maze(dimension);
        Node entry = maze.getAnEntry();
        assert entry != null;

        Calabash brother = new Calabash(new Color(204, 0, 0), world);
        player = new Player(maze, world, entry.x, entry.y, brother);

        initializeWorldTiles();
        world.put(brother, entry.x, entry.y);

    }

    private void initializeWorldTiles() {
        Wall wall = new Wall(world);
        Floor floor = new Floor(world);

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (maze.isFloor(i, j)) {
                    world.put(floor, i, j);
                } else {
                    world.put(wall, i, j);
                }
            }
        }
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int i = 0; i < world.getHeight(); i++) {
            for (int j = 0; j < world.getWidth(); j++) {
                terminal.write(world.get(i, j).getGlyph(), j, i, world.get(i, j).getColor());
            }
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {

        Direction direction = null;
        switch (key.getKeyCode()) {
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

        if (direction != null && player.canMove(direction)) {
            player.doMove(direction);
        }

        return this;
    }

}
