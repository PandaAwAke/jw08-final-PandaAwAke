package com.pandaawake.calabashbros;

public class World {

    private int width, height;

    private Tile<Thing>[][] tiles;

    public World(int width, int height) {
        this.width = width;
        this.height = height;

        if (tiles == null) {
            tiles = new Tile[width][height];
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = new Tile<>(i, j);
                tiles[i][j].setThing(new Floor(this));
            }
        }
    }

    public Thing get(int x, int y) {
        return this.tiles[x][y].getThing();
    }

    public void put(Thing t, int x, int y) {
        this.tiles[x][y].setThing(t);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
