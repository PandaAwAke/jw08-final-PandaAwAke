package com.pandaawake.scene;

import com.pandaawake.renderer.RenderCommand;
import com.pandaawake.sprites.MovableSprite;
import com.pandaawake.sprites.Sprite;
import com.pandaawake.tiles.Floor;
import com.pandaawake.tiles.Thing;
import com.pandaawake.tiles.Tile;
import com.pandaawake.utils.Direction;
import com.pandaawake.utils.IntPair;
import com.pandaawake.utils.Pair;

import java.awt.event.KeyEvent;
import java.util.*;

public class Scene {
    /**
     * The scene is the place that holds all Things in the game.
     * Note: Every Thing added to the scene should be unique!
     *
     */

    private final Set<Thing> things;
    private final Set<Sprite> sprites;  // This will be sorted by Ypos, smaller Ypos == smaller index, for rendering
    private final GameMap gameMap;

    public Scene(GameMap gameMap) {
        things = new HashSet<>();
        sprites = new HashSet<>();
        this.gameMap = gameMap;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    /** Add and remove in update, to avoid the ConcurrentModificationException when iterating
     * [things] and [sprites]
     */
    Set<Pair<Thing, ArrayList<Tile<Thing>>>> thingsToAdd = new HashSet<>();
    Set<Thing> thingsToRemove = new HashSet<>();
    Set<Thing> thingsToRepaint = new HashSet<>();
    Set<Sprite> spritesToAdd = new HashSet<>();
    Set<Sprite> spritesToRemove = new HashSet<>();
    Set<IntPair> positionsToRepaint = new TreeSet<>();
    // ---------------------- Things ----------------------
    public boolean addThing(Thing thing, ArrayList<Tile<Thing>> tiles) {
        synchronized (this) {
            // Check every tile, avoiding conflict things
            if (things.contains(thing)) {
                return false;
            }
            for (Tile<Thing> tile : tiles) {
                if (tile.getThing() != null) {
                    return false;
                }
            }
            thingsToAdd.add(new Pair<>(thing, tiles));
            return true;
        }
    }

    public boolean addThing(Thing thing, Tile<Thing> tile) {
        ArrayList<Tile<Thing>> tiles = new ArrayList<>();
        tiles.add(tile);
        return addThing(thing, tiles);
    }

    public boolean addThing(Thing thing) {
        return addThing(thing, thing.getTiles());
    }

    public boolean addRepaintThing(Thing thing) {
        synchronized (this) {
            return thingsToRepaint.add(thing);
        }
    }

    public Set<Thing> getThings() {
        return things;
    }

    public boolean removeThing(Thing thing) {
        synchronized (this) {
            if (!things.contains(thing)) {
                return false;
            }
            thingsToRemove.add(thing);
            return true;
        }
    }


    // ---------------------- Sprites ----------------------
    public boolean addSprite(Sprite sprite) {
        synchronized (this) {
            return spritesToAdd.add(sprite);
        }
    }

    public Set<Sprite> getSprites() {
        return sprites;
    }

    public boolean removeSprite(Sprite sprite) {
        synchronized (this) {
            return spritesToRemove.add(sprite);
        }
    }


    // ---------------------- Functions ----------------------

    /**
     * This function tells whether the sprite can move to x, y,
     * considering no sprites(moving or stop) collides, tile[x][y] is not blocking,
     * and (x, y) is inside the map.
     * @param sprite
     * @param x
     * @param y
     * @return
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
                if (sprite == otherSprite) {
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



    // ---------------------- Callback Functions ----------------------
    public void OnRender() {
        /**
         * Render sprites
         * This will decide the sequence of rendering
         */
        Set<Sprite> spritesToRender = new TreeSet<>(new Comparator<Sprite>() {
            @Override
            public int compare(Sprite o1, Sprite o2) {
                if (o1 == o2) {
                    return 0;
                }
                if (o1.getY() == o2.getY()) {
                    if (o1.getX() == o2.getX()) {
                        if (!o1.isBlocking()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    } else {
                        return Float.compare(o1.getX(), o2.getX());
                    }
                } else {
                    return Float.compare(o1.getY(), o2.getY());
                }
            }
        });
        spritesToRender.addAll(sprites);
        for (Sprite sprite : spritesToRender) {
            RenderCommand.drawSprite(sprite);
        }

        // Render GameMap
        RenderCommand.drawGameMap(gameMap);

        // Repaint area
        if (positionsToRepaint.size() > 0) {
            RenderCommand.repaintArea(positionsToRepaint);
            positionsToRepaint.clear();
        }
    }

    public void OnUpdate(float timestep) {
        synchronized (this) {
            for (Thing thing : things) {
                thing.OnUpdate(timestep);
            }
            for (Sprite sprite : sprites) {
                sprite.OnUpdate(timestep);
            }

            for (Pair<Thing, ArrayList<Tile<Thing>>> thingAndTiles : thingsToAdd) {
                Thing thing = thingAndTiles.first;
                ArrayList<Tile<Thing>> tiles = thingAndTiles.second;
                for (Tile<Thing> tile : tiles) {
                    thing.addTile(tile);
                }
                things.add(thing);
            }
            for (Thing thing : thingsToRemove) {
                for (Tile<Thing> tile : thing.getTiles()) {
                    positionsToRepaint.add(new IntPair(tile.getxPos(), tile.getyPos()));
                    Floor floor = new Floor();
                    floor.addTile(tile);
                    addThing(floor);
                }
                thing.getTiles().clear();
            }
            for (Thing thing : thingsToRepaint) {
                for (Tile<Thing> tile : thing.getTiles()) {
                    positionsToRepaint.add(new IntPair(tile.getxPos(), tile.getyPos()));
                }
            }
            for (Sprite sprite : spritesToRemove) {
                positionsToRepaint.addAll(sprite.getRenderingBox());
            }

            things.removeAll(thingsToRemove);
            sprites.addAll(spritesToAdd);
            sprites.removeAll(spritesToRemove);

            thingsToAdd.clear();
            thingsToRemove.clear();
            thingsToRepaint.clear();
            spritesToAdd.clear();
            spritesToRemove.clear();
        }
    }

}
