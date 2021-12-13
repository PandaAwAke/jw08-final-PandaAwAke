package com.pandaawake.gourdgame.sprites;

import com.mandas.tiled2d.utils.FloatPair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.tiles.Thing;
import com.mandas.tiled2d.utils.IntPair;

import java.util.HashSet;
import java.util.Set;

public class Bomb extends Sprite {

    private static final float TimeBeforeExploding = Config.TimeBeforeExploding;
    private float elapsedTime = 0.0f;
    private HasBomb owner;
    private boolean explodeImmediately = false; // For scene to control
    private boolean exploded = false;           // For safety

    /**
     * Bomb is a 3x3 destroyer!
     * @param scene
     */
    public Bomb(Scene scene, HasBomb owner, float posX, float posY) {
        super(false, scene, 1, 1);
        setPos(posX, posY);
        this.owner = owner;
        getTileTextureRenderComponent().addPositionAndTexture(new FloatPair(0, 0), Config.TileParser.getTile(7, 8));
    }

    public HasBomb getOwner() {
        return owner;
    }

    public void setExplodeImmediately() {
        explodeImmediately = true;
    }


    @Override
    public void OnUpdate(float timestep) {
        synchronized (this) {
            if (exploded) {
                return;
            }
            elapsedTime += timestep;
            if (elapsedTime >= TimeBeforeExploding || explodeImmediately) {
                // Exploded!
                exploded = true;
                int left = (int) posX - Config.BombExtendedRadius;
                int right = left + 2 * Config.BombExtendedRadius;
                int top = (int) posY - Config.BombExtendedRadius;
                int bottom = top + 2 * Config.BombExtendedRadius;

                Set<Sprite> spritesToRemove = new HashSet<>();

                // Exploding area
                for (int x = Math.max(left, 0); x <= right && x < Config.MapWidth; x++) {
                    for (int y = Math.max(top, 0); y <= bottom && y < Config.MapHeight; y++) {
                        // Remove things which were exploded
                        Thing thing = scene.getGameMap().getTile(x, y).getThing();
                        scene.getSceneUpdater().addRepaintThing(thing);
                        if (thing.OnExplode()) {
                            scene.getSceneUpdater().removeThing(thing);
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

                                // Explode bombs nearby
                                if (sprite instanceof Bomb) {
                                    ((Bomb) sprite).setExplodeImmediately();
                                }
                                break;
                            }
                        }
                    }
                }
                // Remove myself from scene
                spritesToRemove.add(this);
                for (Sprite sprite : spritesToRemove) {
                    scene.getSceneUpdater().removeSprite(sprite);
                }

                owner.bombDestroyed(this);
            }
        }
    }

}