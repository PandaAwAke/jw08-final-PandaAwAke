package com.pandaawake.gourdgame.scene.updater;

import com.mandas.tiled2d.utils.Pair;
import com.pandaawake.gourdgame.network.GameServer;
import com.pandaawake.gourdgame.network.data.action.SceneAction;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.sprites.Sprite;
import com.pandaawake.gourdgame.tiles.Floor;
import com.pandaawake.gourdgame.tiles.Thing;
import com.pandaawake.gourdgame.tiles.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ServerSceneUpdater extends SceneUpdater {

    private final GameServer gameServer;

    public ServerSceneUpdater(GameServer gameServer) {
        super();
        this.gameServer = gameServer;
    }

    @Override
    public boolean updateSprites(Set<Sprite> sprites) {
        // Do nothing
        return false;
    }


    // ---------------------- Functions ----------------------
    @Override
    public void OnRender() {
        // Do nothing
    }

    @Override
    public void OnUpdate(float timestep) {
        synchronized (Scene.class) {
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
                    // positionsToRepaint.add(new IntPair(tile.getxPos(), tile.getyPos()));
                    Floor floor = new Floor(Scene.getNextThingId());
                    floor.addTile(tile);
                    floors.add(floor);
                }
                thing.getTiles().clear();
            }

            scene.getThings().addAll(addedThings);
            if (addedThings.size() > 0) {
                gameServer.sendAction(new SceneAction.AddThings(-1, addedThings));
            }
            scene.getThings().removeAll(thingsToRemove);
            if (thingsToRemove.size() > 0) {
                gameServer.sendAction(new SceneAction.RemoveThings(-1, thingsToRemove));
            }
            scene.getThings().addAll(floors);

            if (thingsToUpdate.size() > 0) {
                gameServer.sendAction(new SceneAction.UpdateThings(-1, thingsToUpdate));
            }

            scene.getSprites().addAll(spritesToAdd);
            if (spritesToAdd.size() > 0) {
                gameServer.sendAction(new SceneAction.AddSprites(-1, spritesToAdd));
            }
            scene.getSprites().removeAll(spritesToRemove);
            if (spritesToRemove.size() > 0) {
                gameServer.sendAction(new SceneAction.RemoveSprites(-1, spritesToRemove));
            }

            thingsToAdd.clear();
            thingsToRemove.clear();
            thingsToUpdate.clear();
            spritesToAdd.clear();
            spritesToRemove.clear();

            if (scene.getSprites().size() > 0) {
                gameServer.sendAction(new SceneAction.UpdateSprites(-1, scene.getSprites()));
            }
        }
    }

}
