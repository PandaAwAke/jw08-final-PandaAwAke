package com.pandaawake.gamemap;

import com.pandaawake.Config;
import com.pandaawake.tiles.Floor;
import com.pandaawake.tiles.Thing;
import com.pandaawake.tiles.Wall;

class SceneCreator {

    private Scene scene;

    /**
     * 1: Floor
     * 2: Wall
     */
    private final int[][] tileMap = {
            {2, 2, 2, 1, 1, 1, 2, 1, 1, 0},
            {2, 1, 2, 1, 1, 1, 2, 1, 1, 0},
            {2, 1, 2, 1, 2, 2, 2, 1, 1, 0},
            {2, 1, 2, 1, 1, 1, 2, 1, 1, 0},
            {1, 1, 1, 1, 2, 1, 2, 1, 1, 0},
            {1, 1, 1, 1, 2, 1, 1, 1, 1, 0},
            {2, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {2, 2, 2, 2, 1, 1, 2, 2, 2, 0},
            {2, 1, 2, 1, 1, 1, 1, 1, 2, 0},
            {2, 1, 1, 1, 1, 1, 1, 1, 1, 0}
    };

    public SceneCreator(Scene scene) {
        this.scene = scene;
    }

    public void initializeScene() {
        initializeTiles();
        initializeSprites();
    }

    private void initializeTiles() {
        // TODO: Support non 1x1 tile thing
        for (int y = 0; y < Config.MapHeight; y++) {
            for (int x = 0; x < Config.MapWidth; x++) {
                Thing thing = getNewThing(x, y);
                if (thing != null) {
                    scene.addThing(thing, scene.getGameMap().getTile(x, y));
                }
            }
        }
    }

    private void initializeSprites() {
        // TODO: Move here from GameSceneScreen
    }

    private Thing getNewThing(int x, int y) {
        int tileKind = tileMap[y][x];
        switch (tileKind) {
            case 1:
                return new Floor();
            case 2:
                return new Wall();
            default:
                return null;
        }
    }


}