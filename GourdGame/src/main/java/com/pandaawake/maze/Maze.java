package com.pandaawake.maze;

public class Maze {

    MazeGenerator generator;
    int[][] maze;
    int dim;

    public Maze(int dim) {
        this.dim = dim;
        generator = new MazeGenerator(dim);
        generator.generateMaze();
        maze = generator.getMaze();
    }

    public boolean isFloor(int i, int j) {
        return maze[i][j] == 1;
    }

    public boolean isWall(int i, int j) {
        return maze[i][j] == 0;
    }

    public boolean insideMaze(int i, int j) {
        return (i >= 0) && (i < dim) && (j >= 0) && (j < dim);
    }

    public Node getAnEntry() {
        // for (int i = 0; i < dim; i++) {
        // for (int j = 0; j < dim; j++) {
        // if (isFloor(i, j)) {
        // return new Node(i, j);
        // }
        // }
        // }
        // return null;
        return new Node(0, 0);
    }

}
