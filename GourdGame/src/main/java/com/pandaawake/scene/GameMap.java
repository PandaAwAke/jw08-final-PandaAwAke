package com.pandaawake.scene;

import com.pandaawake.tiles.*;

public class GameMap {
    /**
     * This class only handles Tiles, and class Scene will handle Things.
     */
    private int width, height;

    private Tile<Thing>[][] tiles;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;

        tiles = new Tile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile<>(x, y);
            }
        }
    }

    public Tile<Thing> getTile(int x, int y) {
        return tiles[x][y];
    }

    public void setTile(int x, int y, Tile<Thing> tile) {
        tiles[x][y] = tile;
        tile.setxPos(x);
        tile.setyPos(y);
    }

    public boolean insideMap(int x, int y) {
        return (x >= 0) && (x < width) && (y >= 0) && (y < height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }



    public void resetAll() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y].setxPos(x);
                tiles[x][y].setyPos(y);
                tiles[x][y].setThing(null);
            }
        }
    }
    
}
