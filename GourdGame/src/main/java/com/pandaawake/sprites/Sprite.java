package com.pandaawake.sprites;

import java.util.Set;
import java.util.TreeSet;

import com.pandaawake.Config;
import com.pandaawake.gamemap.Scene;
import com.pandaawake.renderer.TileTexture;
import com.pandaawake.utils.IntPair;

public class Sprite extends TileTexture {
    /**
     * Sprite is a movable/interactive thing in the scene.
     * The rendering of the sprite should not cover the rendering of tiles.
     */

    protected float posX, posY;                             // Left-top position
    protected Scene scene;
    protected int spriteWidth, spriteHeight;                // Use for collision box
    protected int spriteRenderWidth, spriteRenderHeight;    // Use for rendering
    protected Status status = Status.Ok;                    // The sprite's status

    public enum Status {
        Ok, Moving
    }



    // TODO: Distinguish Rendering area and Collision area
    public Sprite(Scene scene, int spriteWidth, int spriteHeight, int spriteRenderWidth, int spriteRenderHeight) {
        this.scene = scene;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.spriteRenderWidth = spriteRenderWidth;
        this.spriteRenderHeight = spriteRenderHeight;
        this.posX = 0;
        this.posY = 0;
    }

    public Sprite(Scene scene, int spriteWidth, int spriteHeight) {
        this(scene, spriteWidth, spriteHeight, spriteWidth, spriteHeight);
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
    public void setX(float x) {
        posX = x;
    }
    public void setY(float y) {
        posY = y;
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
                collisionBox.add(scene.getGameMap().getTile(x, y).getIntPair());
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
                renderingBox.add(scene.getGameMap().getTile(x, y).getIntPair());
            }
        }
        return renderingBox;
    }


    public void OnUpdate(float timestep) {
        
    }
}
