package com.pandaawake.gourdgame.sprites;

import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.utils.DataUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PlayableSprite extends MovableSprite implements HasBomb {

    public static final int SNAKE = 0;
    public static final int CALABASH = 1;

    protected int lives;
    protected Set<Bomb> bombs = new HashSet<>();
    protected int maxBombs;

    public PlayableSprite(int lives, int maxBombs, boolean blocking, Scene scene, float movingSpeed, int spriteWidth, int spriteHeight) {
        super(blocking, scene, movingSpeed, spriteWidth, spriteHeight);
        this.lives = lives;
        this.maxBombs = maxBombs;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        if (this instanceof Snake) {
            oStream.write(DataUtils.intToBytes(SNAKE));
        } else {
            oStream.write(DataUtils.intToBytes(CALABASH));
        }
        oStream.write(DataUtils.floatToBytes(posX));
        oStream.write(DataUtils.floatToBytes(posY));
        oStream.write(DataUtils.intToBytes(spriteWidth));
        oStream.write(DataUtils.intToBytes(spriteHeight));
        oStream.write(DataUtils.intToBytes(spriteRenderWidth));
        oStream.write(DataUtils.intToBytes(spriteRenderHeight));
        oStream.write(DataUtils.intToBytes((blocking ? 1 : 0)));

        oStream.write(DataUtils.floatToBytes(movingSpeed));
        oStream.write(DataUtils.floatToBytes(targetDeltaPos.first));
        oStream.write(DataUtils.floatToBytes(targetDeltaPos.second));
        oStream.write(DataUtils.floatToBytes(targetPos.first));
        oStream.write(DataUtils.floatToBytes(targetPos.second));
        oStream.write(DataUtils.floatToBytes(movingTime));
        oStream.write(DataUtils.intToBytes(status.number));

        return oStream.toByteArray();
    }

    public static PlayableSprite parseBytes(byte[] bytes, Scene scene) throws IOException {
        ByteArrayInputStream iStream = new ByteArrayInputStream(bytes);
        byte[] subBytes = new byte[4];

        PlayableSprite sprite = null;
        iStream.read(subBytes); int type = DataUtils.bytesToInt(subBytes);
        switch (type) {
            case SNAKE:
                sprite = new Snake(scene);
                break;
            case CALABASH:
                sprite = new Calabash(scene);
                break;
        }
        iStream.read(subBytes); sprite.setX(DataUtils.bytesToFloat(subBytes));
        iStream.read(subBytes); sprite.setY(DataUtils.bytesToFloat(subBytes));
        iStream.read(subBytes); sprite.spriteWidth = DataUtils.bytesToInt(subBytes);
        iStream.read(subBytes); sprite.spriteHeight = DataUtils.bytesToInt(subBytes);
        iStream.read(subBytes); sprite.spriteRenderWidth = DataUtils.bytesToInt(subBytes);
        iStream.read(subBytes); sprite.spriteRenderHeight = DataUtils.bytesToInt(subBytes);
        iStream.read(subBytes); sprite.blocking = (DataUtils.bytesToInt(subBytes) == 1);
        iStream.read(subBytes); sprite.movingSpeed = DataUtils.bytesToFloat(subBytes);
        iStream.read(subBytes); sprite.targetDeltaPos.first = DataUtils.bytesToFloat(subBytes);
        iStream.read(subBytes); sprite.targetDeltaPos.second = DataUtils.bytesToFloat(subBytes);
        iStream.read(subBytes); sprite.targetPos.first = DataUtils.bytesToFloat(subBytes);
        iStream.read(subBytes); sprite.targetPos.second = DataUtils.bytesToFloat(subBytes);
        iStream.read(subBytes); sprite.movingTime = DataUtils.bytesToFloat(subBytes);
        iStream.read(subBytes); sprite.status = Status.fromNumber(DataUtils.bytesToInt(subBytes));

        return sprite;
    }

    public int getLives() {
        return lives;
    }

    @Override
    public Set<Bomb> getBombs() {
        return bombs;
    }

    @Override
    public boolean canSetBomb() {
        return bombs.size() < maxBombs;
    }


    @Override
    public boolean setNewBomb() {
        return false;
    }

    @Override
    public void bombDestroyed(Bomb bomb) {
        bombs.remove(bomb);
    }


}
