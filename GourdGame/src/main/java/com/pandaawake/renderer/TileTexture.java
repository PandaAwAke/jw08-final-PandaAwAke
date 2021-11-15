package com.pandaawake.renderer;

import java.util.ArrayList;

public class TileTexture {
    /** If texture is 2*3, and its chars are:
     * 1 2
     * 3 4
     * 5 6
     * Then glyphs should be <1, 3, 5, 2, 4, 6>.
     *
     */
    protected ArrayList<Character> glyphs;    // Every char inside is a tile's glyph
    public void setTileTexture(ArrayList<Character> glyphs) {
        this.glyphs = glyphs;
    }
    public void setTileTexture(char glyph) {
        glyphs = new ArrayList<>();
        glyphs.add(glyph);
    }
    public ArrayList<Character> getGlyphs() {
        if (glyphs == null) {
            throw new NullPointerException("Glyphs is null!");
        }
        if (glyphs.size() == 0) {
            throw new IllegalStateException("Glyphs is empty! setTileTexture before rendering!");
        }
        return glyphs;
    }

}
