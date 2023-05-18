// ICSTP Assignment 1: Code Review

package com.pandaawake.gourdgame.sprites;

import com.mandas.tiled2d.core.Log;
import com.mandas.tiled2d.scene.Entity;
import com.mandas.tiled2d.scene.component.ComponentFactory;
import com.mandas.tiled2d.scene.component.TileTextureRenderComponent;
import com.mandas.tiled2d.scene.component.TransformComponent;
import com.mandas.tiled2d.utils.pair.FloatPair;
import com.mandas.tiled2d.utils.pair.IntPair;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.utils.DataUtils;
import com.pandaawake.gourdgame.utils.ToBytes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public abstract class Sprite extends Entity implements ToBytes {

    public static final int SNAKE = 1;
    public static final int CALABASH = 2;
    public static final int BOMB = 3;
    public static final int BOMB2 = 4;

    /**
     * Sprite is a movable/interactive thing in the scene.
     * The rendering of the sprite should not cover the rendering of tiles.
     */
    protected int id;
    protected float posX, posY;                             // Left-top position
    protected Scene scene;
    protected int spriteWidth, spriteHeight;                // Use for collision box
    protected int spriteRenderWidth, spriteRenderHeight;    // Use for rendering
    protected boolean blocking;



    // TODO: Distinguish Rendering area and Collision area
    public Sprite(int id, boolean blocking, Scene scene, int spriteWidth, int spriteHeight, int spriteRenderWidth, int spriteRenderHeight) {
        this.id = id;
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
        this.addComponent(ComponentFactory.createComponent(TileTextureRenderComponent.class));
        this.addComponent(ComponentFactory.createComponent(TransformComponent.class));
    }

    public Sprite(int id, boolean blocking, Scene scene, int spriteWidth, int spriteHeight) {
        this(id, blocking, scene, spriteWidth, spriteHeight, spriteWidth, spriteHeight);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        if (this instanceof Snake) {
            oStream.write(DataUtils.intToBytes(SNAKE));
        } else if (this instanceof Calabash) {
            oStream.write(DataUtils.intToBytes(CALABASH));
        } else if (this instanceof Bomb2) {
            oStream.write(DataUtils.intToBytes(BOMB2));
        } else {
            oStream.write(DataUtils.intToBytes(BOMB));
        }
        oStream.write(DataUtils.intToBytes(id));
        oStream.write(DataUtils.floatToBytes(posX));
        oStream.write(DataUtils.floatToBytes(posY));
        oStream.write(DataUtils.intToBytes(spriteWidth));
        oStream.write(DataUtils.intToBytes(spriteHeight));
        oStream.write(DataUtils.intToBytes(spriteRenderWidth));
        oStream.write(DataUtils.intToBytes(spriteRenderHeight));
        oStream.write(DataUtils.intToBytes((blocking ? 1 : 0)));
        return oStream.toByteArray();
    }

    public static Sprite parseBytes(byte[] data, Scene scene) throws IOException {
        ByteArrayInputStream iStream = new ByteArrayInputStream(data);
        byte[] fourBytes = new byte[4];

        Sprite sprite = null;
        iStream.read(fourBytes); int type = DataUtils.bytesToInt(fourBytes);
        switch (type) {
            case SNAKE:
                sprite = new Snake(-1, scene);
                break;
            case CALABASH:
                sprite = new Calabash(-1, scene);
                break;
            case BOMB:
                sprite = new Bomb(-1, scene, -1, 0, 0);
                break;
            case BOMB2:
                sprite = new Bomb2(-1, scene, -1, 0, 0);
                break;
            default:
                Log.app().error("PlayableSprite parseBytes(): illegal type number!");
                return null;
        }
        iStream.read(fourBytes); sprite.setId(DataUtils.bytesToInt(fourBytes));
        iStream.read(fourBytes); sprite.setX(DataUtils.bytesToFloat(fourBytes));
        iStream.read(fourBytes); sprite.setY(DataUtils.bytesToFloat(fourBytes));
        iStream.read(fourBytes); sprite.spriteWidth = DataUtils.bytesToInt(fourBytes);
        iStream.read(fourBytes); sprite.spriteHeight = DataUtils.bytesToInt(fourBytes);
        iStream.read(fourBytes); sprite.spriteRenderWidth = DataUtils.bytesToInt(fourBytes);
        iStream.read(fourBytes); sprite.spriteRenderHeight = DataUtils.bytesToInt(fourBytes);
        iStream.read(fourBytes); sprite.blocking = (DataUtils.bytesToInt(fourBytes) == 1);

        if (sprite instanceof PlayableSprite) {
            iStream.read(fourBytes);
            ((PlayableSprite) sprite).movingSpeed = DataUtils.bytesToFloat(fourBytes);
            iStream.read(fourBytes);
            ((PlayableSprite) sprite).targetDeltaPos.first = DataUtils.bytesToFloat(fourBytes);
            iStream.read(fourBytes);
            ((PlayableSprite) sprite).targetDeltaPos.second = DataUtils.bytesToFloat(fourBytes);
            iStream.read(fourBytes);
            ((PlayableSprite) sprite).targetPos.first = DataUtils.bytesToFloat(fourBytes);
            iStream.read(fourBytes);
            ((PlayableSprite) sprite).targetPos.second = DataUtils.bytesToFloat(fourBytes);
            iStream.read(fourBytes);
            ((PlayableSprite) sprite).movingTime = DataUtils.bytesToFloat(fourBytes);
            iStream.read(fourBytes);
            ((PlayableSprite) sprite).status = MovableSprite.Status.fromNumber(DataUtils.bytesToInt(fourBytes));
            iStream.read(fourBytes);
            ((PlayableSprite) sprite).lives = DataUtils.bytesToInt(fourBytes);
        } else if (sprite instanceof Bomb) {
            iStream.read(fourBytes);
            ((Bomb) sprite).elapsedTime = DataUtils.bytesToFloat(fourBytes);
            iStream.read(fourBytes);
            ((Bomb) sprite).ownerId = DataUtils.bytesToInt(fourBytes);
            iStream.read(fourBytes);
            ((Bomb) sprite).explodeImmediately = (DataUtils.bytesToInt(fourBytes) == 1);
            iStream.read(fourBytes);
            ((Bomb) sprite).exploded = (DataUtils.bytesToInt(fourBytes) == 1);
        }

        return sprite;
    }

    public void updateFromAnotherSprite(Sprite sprite) {
        this.setPos(sprite.posX, sprite.posY);
        this.spriteWidth = sprite.spriteWidth;
        this.spriteHeight = sprite.spriteHeight;
        this.spriteRenderWidth = sprite.spriteRenderWidth;
        this.spriteRenderHeight = sprite.spriteRenderHeight;
        this.blocking = sprite.blocking;
    }


}
