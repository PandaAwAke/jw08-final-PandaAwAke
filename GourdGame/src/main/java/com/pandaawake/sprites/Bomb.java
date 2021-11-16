package com.pandaawake.sprites;

import com.pandaawake.Config;
import com.pandaawake.scene.Scene;
import com.pandaawake.tiles.Thing;
import com.pandaawake.utils.IntPair;
import com.pandaawake.utils.UtilFunctions;

import java.util.HashSet;
import java.util.Set;

public class Bomb extends Sprite {

    private static final float TimeBeforeExploding = Config.TimeBeforeExploding;
    private float elapsedTime = 0.0f;
    private HasBomb owner;

    /**
     * Bomb is a 3x3 destroyer!
     * @param scene
     */
    public Bomb(Scene scene, HasBomb owner, float posX, float posY) {
        super(false, scene, 1, 1);
        this.posX = posX;
        this.posY = posY;
        this.owner = owner;
        setTileTexture(UtilFunctions.PositionInTilesToIndex(7, 8));
    }

    public HasBomb getOwner() {
        return owner;
    }

    @Override
    public void OnUpdate(float timestep) {
        elapsedTime += timestep;
        if (elapsedTime >= TimeBeforeExploding) {
            // Exploded!
            int left = (int) Math.round(posX) - 1;
            int right = left + 2;
            int top = (int) Math.round(posY) - 1;
            int bottom = top + 2;

            Set<Sprite> spritesToRemove = new HashSet<>();

            // Exploding area
            for (int x = Math.max(left, 0); x <= right && x < Config.MapWidth; x++) {
                for (int y = Math.max(top, 0); y <= bottom && y < Config.MapHeight; y++) {
                    // Remove things which were exploded
                    Thing thing = scene.getGameMap().getTile(x, y).getThing();
                    if (thing.OnExplode()) {
                        scene.removeThing(thing);
                    }

                    // Remove sprites which were exploded
                    for (Sprite sprite : scene.getSprites()) {
                        if (sprite == this) {
                            continue;
                        }
                        if (sprite.getCollisionBox().contains(new IntPair(x, y))) {
                            if (sprite.OnExplode(this)) {
                                spritesToRemove.add(sprite);
                            }
                        }
                    }
                }
            }
            // Remove myself
            spritesToRemove.add(this);
            for (Sprite sprite : spritesToRemove) {
                scene.removeSprite(sprite);
            }

            owner.bombDestroyed();
        }

    }

}
