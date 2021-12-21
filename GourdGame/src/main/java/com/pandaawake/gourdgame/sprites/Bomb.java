package com.pandaawake.gourdgame.sprites;

import com.mandas.tiled2d.core.Log;
import com.mandas.tiled2d.utils.FloatPair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.tiles.Thing;
import com.mandas.tiled2d.utils.IntPair;
import com.pandaawake.gourdgame.utils.DataUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Bomb extends Sprite {

    protected static final float TimeBeforeExploding = Config.TimeBeforeExploding;
    protected float elapsedTime = 0.0f;
    protected int ownerId;
    protected boolean explodeImmediately = false; // For scene to control
    protected boolean exploded = false;           // For safety

    /**
     * Bomb is a 3x3 destroyer!
     */
    public Bomb(int id, Scene scene, int ownerId, float posX, float posY) {
        super(id, false, scene, 1, 1);
        setPos(posX, posY);
        this.ownerId = ownerId;
        getTileTextureRenderComponent().addPositionAndTexture(new FloatPair(0, 0), Config.TileParser.getTile(7, 8));
    }

    public int getOwnerId() {
        return ownerId;
    }

    public PlayableSprite getOwner() {
        PlayableSprite matchedSprite = null;
        for (Sprite sceneSprite : scene.getSprites()) {
            if (sceneSprite instanceof PlayableSprite && sceneSprite.getId() == ownerId) {
                matchedSprite = (PlayableSprite) sceneSprite;
                break;
            }
        }
        return matchedSprite;
    }

    public void setExplodeImmediately() {
        explodeImmediately = true;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();

        oStream.write(super.toBytes());

        oStream.write(DataUtils.floatToBytes(elapsedTime));
        oStream.write(DataUtils.intToBytes(ownerId));
        oStream.write(DataUtils.intToBytes(explodeImmediately ? 1 : 0));
        oStream.write(DataUtils.intToBytes(exploded ? 1 : 0));

        return oStream.toByteArray();
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
                        scene.getSceneUpdater().addUpdateThing(thing);
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

                // Find its owner
                PlayableSprite matchedSprite = getOwner();
                if (matchedSprite != null) {
                    matchedSprite.bombDestroyed(this);
                } else {
                    Log.app().error(getClass().getName() + ": no matched sprite of bomb owner!");
                }
            }
        }
    }

    @Override
    public void updateFromAnotherSprite(Sprite sprite) {
        super.updateFromAnotherSprite(sprite);
        this.elapsedTime = ((Bomb) sprite).elapsedTime;
        this.ownerId = ((Bomb) sprite).ownerId;
        this.explodeImmediately = ((Bomb) sprite).explodeImmediately;
        this.exploded = ((Bomb) sprite).exploded;
    }

}
