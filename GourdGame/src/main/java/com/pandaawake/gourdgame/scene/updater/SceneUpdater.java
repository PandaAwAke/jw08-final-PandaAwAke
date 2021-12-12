package com.pandaawake.gourdgame.scene.updater;

import com.mandas.tiled2d.utils.IntPair;
import com.mandas.tiled2d.utils.Pair;
import com.pandaawake.gourdgame.render.RenderCommand;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.sprites.Sprite;
import com.pandaawake.gourdgame.tiles.Floor;
import com.pandaawake.gourdgame.tiles.Thing;
import com.pandaawake.gourdgame.tiles.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public abstract class SceneUpdater {

    protected Scene scene = null;

    public SceneUpdater() {

    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /** Add and remove in update, to avoid the ConcurrentModificationException when iterating
     * [things] and [sprites]
     */
    protected Set<Pair<Thing, ArrayList<Tile>>> thingsToAdd = new HashSet<>();
    protected Set<Thing> thingsToRemove = new HashSet<>();
    protected Set<Thing> thingsToRepaint = new HashSet<>();
    protected Set<Sprite> spritesToAdd = new HashSet<>();
    protected Set<Sprite> spritesToRemove = new HashSet<>();
    protected Set<IntPair> positionsToRepaint = new TreeSet<>();

    // ---------------------- Things ----------------------
    public boolean addThing(Thing thing, ArrayList<Tile> tiles) {
        synchronized (scene) {
            // Check every tile, avoiding conflict things
            if (scene.getThings().contains(thing)) {
                return false;
            }
            for (Tile tile : tiles) {
                if (tile.getThing() != null) {
                    return false;
                }
            }
            thingsToAdd.add(new Pair<>(thing, tiles));
            return true;
        }
    }

    public boolean addThing(Thing thing, Tile tile) {
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(tile);
        return addThing(thing, tiles);
    }

    public boolean addThing(Thing thing) {
        return addThing(thing, thing.getTiles());
    }

    public boolean addRepaintThing(Thing thing) {
        synchronized (scene) {
            return thingsToRepaint.add(thing);
        }
    }

    public boolean removeThing(Thing thing) {
        synchronized (scene) {
            if (!scene.getThings().contains(thing)) {
                return false;
            }
            thingsToRemove.add(thing);
            return true;
        }
    }


    // ---------------------- Sprites ----------------------
    public boolean addSprite(Sprite sprite) {
        synchronized (scene) {
            return spritesToAdd.add(sprite);
        }
    }

    public boolean removeSprite(Sprite sprite) {
        synchronized (scene) {
            return spritesToRemove.add(sprite);
        }
    }


    // ---------------------- Functions ----------------------
    public void resetAll() {
        synchronized (scene) {
            thingsToAdd.clear();
            thingsToRemove.clear();
            thingsToRepaint.clear();
            spritesToAdd.clear();
            spritesToRemove.clear();
            positionsToRepaint.clear();
        }
    }

    public void OnRender() {
        synchronized (scene) {
            // Repaint area
            if (positionsToRepaint.size() > 0) {
                RenderCommand.repaintPosition(positionsToRepaint);
                positionsToRepaint.clear();
            }
        }
    }

    public void OnUpdate(float timestep) {
        synchronized (scene) {
            for (Pair<Thing, ArrayList<Tile>> thingAndTiles : thingsToAdd) {
                Thing thing = thingAndTiles.first;
                ArrayList<Tile> tiles = thingAndTiles.second;
                for (Tile tile : tiles) {
                    thing.addTile(tile);
                }
                scene.getThings().add(thing);
            }

            for (Thing thing : thingsToRemove) {
                for (Tile tile : thing.getTiles()) {
                    positionsToRepaint.add(new IntPair(tile.getxPos(), tile.getyPos()));
                    Floor floor = new Floor();
                    floor.addTile(tile);
                    addThing(floor);
                }
                thing.getTiles().clear();
            }

            for (Thing thing : thingsToRepaint) {
                for (Tile tile : thing.getTiles()) {
                    positionsToRepaint.add(new IntPair(tile.getxPos(), tile.getyPos()));
                }
            }

            scene.getThings().removeAll(thingsToRemove);
            scene.getSprites().addAll(spritesToAdd);
            scene.getSprites().removeAll(spritesToRemove);

            thingsToAdd.clear();
            thingsToRemove.clear();
            thingsToRepaint.clear();
            spritesToAdd.clear();
            spritesToRemove.clear();
        }
    }


}
