package com.pandaawake.renderer;

import java.util.ArrayList;

public class TileTexture {
    /** If texture is 2*3, and its ints are:
     * 1 2
     * 3 4
     * 5 6
     * Then glyphs should be <1, 2, 3, 4, 5, 6>.
     *
     */
    protected ArrayList<Integer> glyphs;    // Every int inside is a tile's glyph
    public void setTileTexture(ArrayList<Integer> glyphs) {
        this.glyphs = glyphs;
    }
    public void setTileTexture(int glyph) {
        glyphs = new ArrayList<>();
        glyphs.add(glyph);
    }
    public ArrayList<Integer> getGlyphs() {
        if (glyphs == null) {
            throw new NullPointerException("Glyphs is null!");
        }
        if (glyphs.size() == 0) {
            throw new IllegalStateException("Glyphs is empty! setTileTexture before rendering!");
        }
        return glyphs;
    }

}
