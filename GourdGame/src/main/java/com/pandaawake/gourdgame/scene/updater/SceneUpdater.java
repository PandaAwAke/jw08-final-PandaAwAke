package com.pandaawake.gourdgame.scene.updater;

import com.mandas.tiled2d.utils.pair.IntPair;
import com.mandas.tiled2d.utils.pair.Pair;
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

public class SceneUpdater {

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
    protected Set<Thing> thingsToUpdate = new HashSet<>();
    protected Set<Sprite> spritesToAdd = new HashSet<>();
    protected Set<Sprite> spritesToRemove = new HashSet<>();
    protected Set<Sprite> spritesToUpdate = new HashSet<>();
    protected Set<IntPair> positionsToRepaint = new TreeSet<>();

    // ---------------------- Things ----------------------
    public boolean addThing(Thing thing, ArrayList<Tile> tiles) {
        synchronized (this) {
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

    public boolean addUpdateThing(Thing thing) {
        synchronized (this) {
            return thingsToUpdate.add(thing);
        }
    }

    public boolean updateThings(Set<Thing> things) {
        synchronized (this) {
            return thingsToUpdate.addAll(things);
        }
    }

    public boolean removeThing(Thing thing) {
        synchronized (this) {
            if (!scene.getThings().contains(thing)) {
                return false;
            }
            thingsToRemove.add(thing);
            return true;
        }
    }

    public boolean removeThingById(int id) {
        synchronized (this) {
            for (Thing thing : scene.getThings()) {
                if (thing.getId() == id) {
                    thingsToRemove.add(thing);
                    return true;
                }
            }
            return false;
        }
    }


    // ---------------------- Sprites ----------------------
    public boolean addSprite(Sprite sprite) {
        synchronized (this) {
            return spritesToAdd.add(sprite);
        }
    }

    public boolean removeSprite(Sprite sprite) {
        synchronized (this) {
            return spritesToRemove.add(sprite);
        }
    }

    public boolean removeSpriteById(int id) {
        synchronized (this) {
            for (Sprite sprite : scene.getSprites()) {
                if (sprite.getId() == id) {
                    spritesToRemove.add(sprite);
                    return true;
                }
            }
            return false;
        }
    }



    public boolean updateSprites(Set<Sprite> sprites) {
        synchronized (this) {
            return spritesToUpdate.addAll(sprites);
        }
    }


    // ---------------------- Functions ----------------------
    public void resetAll() {
        synchronized (this) {
            spritesToAdd.clear();
            spritesToRemove.clear();
            spritesToUpdate.clear();
            thingsToAdd.clear();
            thingsToRemove.clear();
            thingsToUpdate.clear();
            positionsToRepaint.clear();
        }
    }

    public void OnRender() {
        synchronized (this) {
            // Repaint area
            if (positionsToRepaint.size() > 0) {
                RenderCommand.repaintPosition(positionsToRepaint);
                positionsToRepaint.clear();
            }
        }
    }

    public void OnUpdate(float timestep) {
        synchronized (this) {
            Set<Thing> addedThings = new HashSet<>();
            Set<Thing> floors = new HashSet<>();

            for (Pair<Thing, ArrayList<Tile>> thingAndTiles : thingsToAdd) {
                Thing thing = thingAndTiles.first;
                ArrayList<Tile> tiles = thingAndTiles.second;
                for (Tile tile : tiles) {
                    thing.addTile(tile);
                }
                addedThings.add(thing);
            }

            for (Thing thing : thingsToRemove) {
                for (Tile tile : thing.getTiles()) {
                    positionsToRepaint.add(new IntPair(tile.getxPos(), tile.getyPos()));
                    Floor floor = new Floor(Scene.getNextThingId());
                    floor.addTile(tile);
                    floors.add(floor);
                }
                thing.getTiles().clear();
            }

            for (Thing thing : thingsToUpdate) {
                for (Tile tile : thing.getTiles()) {
                    positionsToRepaint.add(new IntPair(tile.getxPos(), tile.getyPos()));
                }
            }

            scene.getThings().addAll(addedThings);
            scene.getThings().removeAll(thingsToRemove);
            scene.getThings().addAll(floors);
            scene.getSprites().addAll(spritesToAdd);
            scene.getSprites().removeAll(spritesToRemove);

            for (Sprite sprite : spritesToUpdate) {
                Sprite matchedSprite = null;
                for (Sprite sceneSprite : scene.getSprites()) {
                    if (sprite.getId() == sceneSprite.getId()) {
                        matchedSprite = sceneSprite;
                        break;
                    }
                }
                if (matchedSprite != null) {
                    matchedSprite.reload(sprite);
                }
            }

            for (Thing thing : thingsToUpdate) {
                Thing matchedThing = null;
                for (Thing sceneThing : scene.getThings()) {
                    if (thing.getId() == sceneThing.getId()) {
                        matchedThing = sceneThing;
                        break;
                    }
                }
                if (matchedThing != null) {
                    matchedThing.updateFromAnotherThing(thing);
                }
            }

            spritesToAdd.clear();
            spritesToRemove.clear();
            spritesToUpdate.clear();
            thingsToAdd.clear();
            thingsToRemove.clear();
            thingsToUpdate.clear();

        }
    }



}
