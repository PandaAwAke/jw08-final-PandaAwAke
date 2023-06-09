package com.pandaawake.gourdgame.scene;

import com.mandas.tiled2d.utils.pair.IntPair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.sprites.MovableSprite;
import com.pandaawake.gourdgame.tiles.*;

import java.util.List;
import java.util.Random;

public class SceneTilesInitializer {

    private static final char KIND_FLOOR = '1';
    private static final char KIND_WALL = '2';
    private static final char KIND_TREE = '3';
    private static final char KIND_TWICE_BREAKABLE_WALL = '4';


    private Scene scene;

    /**
     * This function will initialize the tiles for the game scene by [tileMap].
     */
    public void initializeTiles(List<String> tileMap) {
        // TODO: Support non 1x1 tile thing
        for (int y = 0; y < Config.MapHeight; y++) {
            for (int x = 0; x < Config.MapWidth; x++) {
                Tile tile = scene.getGameMap().getTile(x, y);
                if (tile == null) {
                    throw new NullPointerException("Null tile in GameMap! Please create GameMap first.");
                }
                Thing thing = getNewThing(tileMap.get(y).charAt(x));
                if (thing != null) {
                    scene.getSceneUpdater().addThing(thing, tile);
                }
            }
        }
    }

    public void initializeTiles(Level level) {
        initializeTiles(level.levelTileMap);
    }

    public SceneTilesInitializer(Scene scene) {
        this.scene = scene;
    }

    private Thing getNewThing(char c) {
        switch (c) {
            case KIND_FLOOR:
                return new Floor(Scene.getNextThingId());
            case KIND_WALL:
                return new Wall(Scene.getNextThingId());
            case KIND_TREE:
                return new Tree(Scene.getNextThingId());
            case KIND_TWICE_BREAKABLE_WALL:
                return new TwiceBreakableWall(Scene.getNextThingId(), scene);
            default:
                return new Floor(Scene.getNextThingId());
        }
    }

    /**
     * This function will get an available entry (considering no sprites(moving or stop) collides,
     * tile[x][y] is not blocking, and (x, y) is inside the map) for a movable sprite,
     * and it should be called after initializeTiles.
     *
     * Note: The tileMap must have at least one available entry, otherwise this will loop infinitely.
     *
     * @return a legal new movable sprite entry position
     */
    public IntPair getASpriteEntryPositionRandomly(MovableSprite sprite) {
        Random random = new Random();
        if (sprite.getStatus() == MovableSprite.Status.Moving) {
            throw new IllegalStateException("Try to generate an entry for a moving sprite!");
        }
        while (true) {
            int x = random.nextInt(scene.getGameMap().getWidth());
            int y = random.nextInt(scene.getGameMap().getHeight());
            Tile tile = scene.getGameMap().getTile(x, y);
            if (tile == null) {
                throw new NullPointerException("Null tile in GameMap! Please create GameMap first.");
            }
            if (tile.getThing() != null && tile.getThing().isBlocking()) {
                continue;
            }
            if (!scene.spriteCanAddTo(sprite, x, y)) {
                continue;
            }
            return new IntPair(x, y);
        }
    }

}
