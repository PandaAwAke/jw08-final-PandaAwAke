package com.pandaawake.gourdgame.tiles;

import java.util.ArrayList;

import com.mandas.tiled2d.renderer.Texture;
import com.mandas.tiled2d.renderer.TileTextures;

public class Thing {

    // Do not use Set, because array can maintain a sequence
    protected ArrayList<Tile<Thing>> tiles;
    protected boolean blocking;
    protected TileTextures tileTextures;

    public void addTile(Tile<Thing> tile) {
        if (!tiles.contains(tile)) {
            tiles.add(tile);
            tile.setThing(this);
        }
    }

    public ArrayList<Tile<Thing>> getTiles() {
        return tiles;
    }

    public Thing(boolean blocking) {
        tiles = new ArrayList<>();
        this.blocking = blocking;   // Will this thing blocks the player?
        this.tileTextures = new TileTextures();
    }

    public boolean isBlocking() {
        return this.blocking;
    }


    public ArrayList<Texture> getTextures() {
        if (tileTextures.getTextures() != null && tileTextures.getTextures().size() != tiles.size()) {
            throw new IllegalStateException("Textures' size does not equal to the tiles' size!");
        }
        return tileTextures.getTextures();
    }

    /**
     * This function decides whether this thing is destroyed by the bomb
     * @return true if this thing is destroyed by the bomb
     */
    public boolean OnExplode() {
        return false;
    }

    public void OnUpdate(float timestep) {
        
    }
}
