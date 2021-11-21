package com.mandas.tiled2d.renderer;

import java.util.ArrayList;

public class TileTextures {


    /** If texture is 2*3, and its ints are:
     * 1 2
     * 3 4
     * 5 6
     * Then textures should be <1, 2, 3, 4, 5, 6>.
     *
     */
    private ArrayList<Texture> textures;

    public void setTileTexture(ArrayList<Texture> textures) {
        this.textures = textures;
    }

    public void setTileTexture(Texture texture) {
        textures = new ArrayList<>();
        textures.add(texture);
    }

    public ArrayList<Texture> getTextures() {
        if (textures == null) {
            throw new NullPointerException("Textures is null!");
        }
        if (textures.size() == 0) {
            throw new IllegalStateException("Textures is empty! setTileTexture before rendering!");
        }
        return textures;
    }

}
