package com.pandaawake.tiles;

import java.util.ArrayList;

import com.pandaawake.renderer.TileTexture;

public class Thing extends TileTexture {

    // Do not use Set, because array can maintain a sequence
    protected ArrayList<Tile<Thing>> tiles;

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
    }

    private final boolean blocking;

    public boolean isBlocking() {
        return this.blocking;
    }

    @Override
    public ArrayList<Character> getGlyphs() {
        if (glyphs != null && glyphs.size() != tiles.size()) {
            throw new IllegalStateException("Glyphs' size does not equal to the tiles' size!");
        }
        return super.getGlyphs();
    }

    public void OnUpdate(float timestep) {
        
    }
}
