package com.pandaawake.gourdgame.sprites;

import com.mandas.tiled2d.scene.Entity;
import com.mandas.tiled2d.scene.TileTextureRenderComponent;
import com.mandas.tiled2d.scene.TransformComponent;
import com.mandas.tiled2d.utils.FloatPair;
import com.mandas.tiled2d.utils.IntPair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.scene.Scene;

import java.util.Set;
import java.util.TreeSet;

public abstract class Sprite extends Entity {
    /**
     * Sprite is a movable/interactive thing in the scene.
     * The rendering of the sprite should not cover the rendering of tiles.
     */

    protected float posX, posY;                             // Left-top position
    protected Scene scene;
    protected int spriteWidth, spriteHeight;                // Use for collision box
    protected int spriteRenderWidth, spriteRenderHeight;    // Use for rendering
    protected boolean blocking;


    // TODO: Distinguish Rendering area and Collision area
    public Sprite(boolean blocking, Scene scene, int spriteWidth, int spriteHeight, int spriteRenderWidth, int spriteRenderHeight) {
        this.blocking = blocking;
        this.scene = scene;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.spriteRenderWidth = spriteRenderWidth;
        this.spriteRenderHeight = spriteRenderHeight;
        this.posX = 0.0f;
        this.posY = 0.0f;
        //this.tileTextures = new TileTextures();

        // Components
        this.addComponent(new TileTextureRenderComponent());
        this.addComponent(new TransformComponent());
    }

    public Sprite(boolean blocking, Scene scene, int spriteWidth, int spriteHeight) {
        this(blocking, scene, spriteWidth, spriteHeight, spriteWidth, spriteHeight);
    }

//    public ArrayList<Texture> getTextures() {
//        return tileTextures.getTextures();
//    }

    public boolean isBlocking() {
        return blocking;
    }


    public Scene getScene() {
        return scene;
    }

    public float getX() {
        return posX;
    }
    public float getY() {
        return posY;
    }
    public FloatPair getPos() {
        return new FloatPair(posX, posY);
    }
    public void setX(float x) {
        posX = x;
        getTransformComponent().setTranslationX(x);
    }
    public void setY(float y) {
        posY = y;
        getTransformComponent().setTranslationY(y);
    }
    public void setPos(float x, float y) {
        setX(x);
        setY(y);
    }
    public int getSpriteWidth() {
        return spriteWidth;
    }
    public int getSpriteHeight() {
        return spriteHeight;
    }
    public int getSpriteRenderWidth() {
        return spriteRenderWidth;
    }
    public int getSpriteRenderHeight() {
        return spriteRenderHeight;
    }


    // Collision box
    public Set<IntPair> getCollisionBox() {
        int left = (int) Math.round(Math.floor(posX));
        int right = (int) Math.round(Math.ceil(posX)) + spriteWidth - 1;
        int top = (int) Math.round(Math.floor(posY));
        int bottom = (int) Math.round(Math.ceil(posY)) + spriteHeight - 1;
        Set<IntPair> collisionBox = new TreeSet<>();
        for (int x = Math.max(left, 0); x <= right && x < Config.MapWidth; x++) {
            for (int y = Math.max(top, 0); y <= bottom && y < Config.MapHeight; y++) {
                collisionBox.add(new IntPair(x, y));
            }
        }
        return collisionBox;
    }

    // Rendering box
    public Set<IntPair> getRenderingBox() {
        int left = (int) Math.round(Math.floor(posX));
        int right = (int) Math.round(Math.ceil(posX)) + spriteRenderWidth - 1;
        int top = (int) Math.round(Math.floor(posY));
        int bottom = (int) Math.round(Math.ceil(posY)) + spriteRenderHeight - 1;
        Set<IntPair> renderingBox = new TreeSet<>();
        for (int x = Math.max(left, 0); x <= right && x < Config.MapWidth; x++) {
            for (int y = Math.max(top, 0); y <= bottom && y < Config.MapHeight; y++) {
                renderingBox.add(new IntPair(x, y));
            }
        }
        return renderingBox;
    }

    @Override
    public void OnUpdate(float timestep) {}

    public boolean OnExplode(Bomb bomb) {
        return false;
    }

}
