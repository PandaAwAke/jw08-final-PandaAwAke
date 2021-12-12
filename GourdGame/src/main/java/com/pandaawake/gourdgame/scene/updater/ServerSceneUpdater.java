package com.pandaawake.gourdgame.scene.updater;

import com.mandas.tiled2d.utils.Pair;
import com.pandaawake.gourdgame.tiles.Floor;
import com.pandaawake.gourdgame.tiles.Thing;
import com.pandaawake.gourdgame.tiles.Tile;

import java.util.ArrayList;

public class ServerSceneUpdater extends SceneUpdater {

    public ServerSceneUpdater() {
        super();
    }


    // ---------------------- Things ----------------------
    @Override
    public boolean addRepaintThing(Thing thing) {
        // Do nothing
        return true;
    }


    // ---------------------- Functions ----------------------
    public void OnRender() {
        // Do nothing
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
                    // positionsToRepaint.add(new IntPair(tile.getxPos(), tile.getyPos()));
                    Floor floor = new Floor();
                    floor.addTile(tile);
                    addThing(floor);
                }
                thing.getTiles().clear();
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
