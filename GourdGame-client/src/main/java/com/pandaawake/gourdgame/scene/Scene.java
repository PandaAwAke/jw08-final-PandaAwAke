package com.pandaawake.gourdgame.scene;

import com.mandas.tiled2d.utils.IntPair;
import com.pandaawake.gourdgame.render.RenderCommand;
import com.pandaawake.gourdgame.scene.updater.SceneUpdater;
import com.pandaawake.gourdgame.sprites.MovableSprite;
import com.pandaawake.gourdgame.sprites.Sprite;
import com.pandaawake.gourdgame.tiles.Thing;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Scene extends com.mandas.tiled2d.scene.Scene {
    /**
     * The scene is the place that holds all Things in the game.
     * Note: Every Thing added to the scene should be unique!
     *
     */

    final Set<Thing> things;
    final Set<Sprite> sprites;  // This will be sorted by Ypos, smaller Ypos == smaller index, for rendering
    final GameMap gameMap;
    final SceneUpdater sceneUpdater;

    public Scene(GameMap gameMap, SceneUpdater sceneUpdater) {
        super();
        things = new HashSet<>();
        sprites = new HashSet<>();
        this.gameMap = gameMap;
        this.sceneUpdater = sceneUpdater;
        sceneUpdater.setScene(this);
        setEntities(sprites);
    }

    public GameMap getGameMap() {
        return gameMap;
    }
    public Set<Sprite> getSprites() {
        return sprites;
    }
    public Set<Thing> getThings() {
        return things;
    }
    public SceneUpdater getSceneUpdater() {
        return sceneUpdater;
    }


    private static int spriteId = 0, thingId = 0;
    public static int getNextSpriteId() {
        return spriteId++;
    }
    public static int getNextThingId() {
        return thingId++;
    }


    // ---------------------- Functions ----------------------

    /**
     * This function tells whether the sprite can move to x, y,
     * considering no sprites(moving or stop) collides, tile[x][y] is not blocking,
     * and (x, y) is inside the map.
     * @param sprite Sprite to test
     * @param x Position X
     * @param y Position Y
     * @return Can move to?
     */
    public boolean spriteCanMoveTo(MovableSprite sprite, int x, int y) {
        synchronized (this) {
            if (sprite.getStatus() == MovableSprite.Status.Moving) {
                return false;
            }
            if (!gameMap.insideMap(x, y)) {
                return false;
            }
            if (gameMap.getTile(x, y).getThing() != null && gameMap.getTile(x, y).getThing().isBlocking()) {
                return false;
            }

            if (!sprite.isBlocking()) {
                return true;
            }
            // Consider other sprites, get their collision boxes first
            Set<IntPair> unreachablePositions = new TreeSet<>();
            for (Sprite otherSprite : sprites) {
                if (sprite.getId() == otherSprite.getId()) {
                    continue;
                }
                if (!otherSprite.isBlocking()) {
                    continue;
                }
                unreachablePositions.addAll(otherSprite.getCollisionBox());
            }
            for (IntPair position : sprite.tryToMoveCollisionBox(x, y)) {
                if (unreachablePositions.contains(position)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * This function tells whether the sprite can move to x, y,
     * considering no sprites(moving or stop) collides, tile[x][y] is not blocking,
     * and (x, y) is inside the map.
     * @param sprite Sprite to test
     * @param x Position X
     * @param y Position Y
     * @return Can add to this position?
     */
    public boolean spriteCanAddTo(MovableSprite sprite, int x, int y) {
        synchronized (this) {
            if (sprite.getStatus() == MovableSprite.Status.Moving) {
                return false;
            }
            if (!gameMap.insideMap(x, y)) {
                return false;
            }
            if (gameMap.getTile(x, y).getThing() != null && gameMap.getTile(x, y).getThing().isBlocking()) {
                return false;
            }

            if (!sprite.isBlocking()) {
                return true;
            }
            // Consider other sprites, get their collision boxes first
            Set<IntPair> unreachablePositions = new TreeSet<>();
            for (Sprite otherSprite : sprites) {
                if (sprite.getId() == otherSprite.getId()) {
                    continue;
                }
                if (!otherSprite.isBlocking()) {
                    continue;
                }
                unreachablePositions.addAll(otherSprite.getCollisionBox());
            }
            for (IntPair position : sprite.tryToAddCollisionBox(x, y)) {
                if (unreachablePositions.contains(position)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Reset the entire scene.
     */
    public void resetAll() {
        synchronized (this) {
            things.clear();
            sprites.clear();

            sceneUpdater.resetAll();
        }
    }


    // ---------------------- Callback Functions ----------------------
    public void OnRender() {
        synchronized (this) {
            // Render GameMap
            RenderCommand.drawGameMap(gameMap);

            // Scene Updater
            sceneUpdater.OnRender();

//            @Override
//            public int compareTo(Sprite o) {
//                /*
//                 * This will decide the sequence of rendering
//                 */
//                if (this.getId() == o.getId()) {
//                    return 0;
//                }
//
//                if (!this.blocking && o.blocking) {
//                    return -1;
//                } else if (this.blocking && !o.blocking) {
//                    return 1;
//                }
//
//                if (this.getY() == o.getY()) {
//                    if (this.getX() == o.getX()) {
//                        return Integer.compare(this.hashCode(), o.hashCode());
//                    } else {
//                        return Float.compare(this.getX(), o.getX());
//                    }
//                } else {
//                    return Float.compare(this.getY(), o.getY());
//                }
//            }
        }
    }

    @Override
    public void OnUpdate(float timestep) {
        synchronized (this) {
            // Set the sequence for render
            super.setEntities(sprites);

            super.OnUpdate(timestep);

            for (Thing thing : things) {
                thing.OnUpdate(timestep);
            }

            sceneUpdater.OnUpdate(timestep);
        }
    }

}
