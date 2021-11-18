package com.pandaawake.renderer;

import java.util.ArrayList;

public class TileTexture {
    /** If texture is 2*3, and its ints are:
     * 1 2
     * 3 4
     * 5 6
     * Then textures should be <1, 2, 3, 4, 5, 6>.
     *
     */
    protected ArrayList<Texture> textures;
    public void setTileTexture(ArrayList<Texture> textures) {
        this.textures = textures;
    }
    public void setTileTexture(Texture texture) {
        textures = new ArrayList<>();
        textures.add(texture);
    }
    public ArrayList<Texture> getGlyphs() {
        if (textures == null) {
            throw new NullPointerException("textures is null!");
        }
        if (textures.size() == 0) {
            throw new IllegalStateException("textures is empty! setTileTexture before rendering!");
        }
        return textures;
    }

}
