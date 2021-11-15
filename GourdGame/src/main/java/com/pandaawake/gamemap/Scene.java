package com.pandaawake.gamemap;

import com.pandaawake.sprites.Sprite;
import com.pandaawake.tiles.Thing;
import com.pandaawake.tiles.Tile;

import java.util.*;

public class Scene {
    /**
     * The scene is the place that holds all Things in the game.
     * Note: Every Thing added to the scene should be unique!
     *
     */

    private Set<Thing> things;
    private Set<Sprite> sprites;        // This will be sorted by Ypos, smaller Ypos == smaller index, for rendering
    private GameMap gameMap;
    private SceneCreator sceneCreator;

    public Scene(GameMap gameMap) {
        things = new HashSet<>();
        sprites = new TreeSet<>(new Comparator<Sprite>() {
            @Override
            public int compare(Sprite o1, Sprite o2) {
                if (o1.getY() == o2.getY()) {
                    return Float.compare(o1.getX(), o2.getX());
                } else {
                    return Float.compare(o1.getY(), o2.getY());
                }
            }
        });
        this.gameMap = gameMap;
        sceneCreator = new SceneCreator(this);
        sceneCreator.initializeScene();
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    // ---------------------- Things ----------------------
    public boolean addThing(Thing thing, ArrayList<Tile<Thing>> tiles) {
        // Check every tile, avoiding conflict things
        if (things.contains(thing)) {
            return false;
        }
        for (Tile<Thing> tile : tiles) {
            if (tile.getThing() != null) {
                return false;
            }
        }
        for (Tile<Thing> tile : tiles) {
            thing.addTile(tile);
        }
        things.add(thing);
        return true;
    }

    public boolean addThing(Thing thing, Tile<Thing> tile) {
        ArrayList<Tile<Thing>> tiles = new ArrayList<>();
        tiles.add(tile);
        return addThing(thing, tiles);
    }

    public boolean addThing(Thing thing) {
        return addThing(thing, thing.getTiles());
    }

    public Set<Thing> getThings() {
        return things;
    }

    public boolean removeThing(Thing thing) {
        if (!things.contains(thing)) {
            return false;
        }
        for (Tile<Thing> tile : thing.getTiles()) {
            tile.setThing(null);
        }
        thing.getTiles().clear();
        things.remove(thing);
        return true;
    }


    // ---------------------- Sprites ----------------------
    public boolean addSprite(Sprite sprite) {
        return sprites.add(sprite);
    }

    public Set<Sprite> getSprites() {
        return sprites;
    }

    public boolean removeSprite(Sprite sprite) {
        return sprites.remove(sprite);
    }


    // ---------------------- Functions ----------------------
    public void OnUpdate(float timestep) {
        for (Thing thing : things) {
            thing.OnUpdate(timestep);
        }
        for (Sprite sprite : sprites) {
            sprite.OnUpdate(timestep);
        }
    }

}
