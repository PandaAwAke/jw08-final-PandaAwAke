package com.mandas.tiled2d.scene.component;

import com.mandas.tiled2d.renderer.Texture;
import com.mandas.tiled2d.scene.component.Component;
import com.mandas.tiled2d.utils.pair.FloatPair;
import com.mandas.tiled2d.utils.pair.Pair;

import java.util.ArrayList;

public class TileTextureRenderComponent implements Component {

    @Override
    public int getIdentification() {
        return Component.TileTextureRenderComponentId;
    }

    /** If texture is 2*3:
     * tex1 tex2
     * tex3 tex4
     * tex5 tex6
     *
     * Then textures should be
     * [
     *      ((0.0, 0.0), tex1), ((1.0, 0.0), tex2),
     *      ((0.0, 1.0), tex3), ((1.0, 1.0), tex4),
     *      ((0.0, 2.0), tex5), ((1.0, 2.0), tex6)
     * ]
     *
     */
    private ArrayList<Pair<FloatPair, Texture>> positionsAndTextures;

    public TileTextureRenderComponent() {
        positionsAndTextures = new ArrayList<>();
    }

    public void addPositionAndTexture(FloatPair position, Texture texture) {
        if (position == null || texture == null) {
            throw new IllegalArgumentException("Null position or null texture!");
        }
        positionsAndTextures.add(new Pair<>(position, texture));
    }

    public ArrayList<Pair<FloatPair, Texture>> getPositionsAndTextures() {
        return positionsAndTextures;
    }

}
