package com.pandaawake.gourdgame.sprites;

import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.utils.DataUtils;
import com.pandaawake.gourdgame.utils.Direction;
import com.mandas.tiled2d.utils.pair.FloatPair;
import com.mandas.tiled2d.utils.pair.IntPair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public abstract class MovableSprite extends Sprite {

    protected float movingSpeed = 4.0f;     // How many tiles will this sprite move in 1 second
    protected FloatPair targetDeltaPos = new FloatPair(0.0f, 0.0f);
    protected FloatPair targetPos = new FloatPair(0.0f, 0.0f);
    protected float movingTime = 0.0f;      // How many seconds have this sprite been moving

    protected Status status = Status.Ok;    // The sprite's status

    public enum Status {
        Ok(0), Moving(1);

        public int number = 0;
        Status(int number) {
            this.number = number;
        }

        public static Status fromNumber(int number) {
            switch (number) {
                case 0:
                    return Status.Ok;
                case 1:
                    return Status.Moving;
                default:
                    return null;
            }
        }
    }

    public MovableSprite(int id, boolean blocking, Scene scene, int spriteWidth, int spriteHeight) {
        super(id, blocking, scene, spriteWidth, spriteHeight);
    }

    public MovableSprite(int id, boolean blocking, Scene scene, int spriteWidth, int spriteHeight, int spriteRenderWidth, int spriteRenderHeight) {
        super(id, blocking, scene, spriteWidth, spriteHeight, spriteRenderWidth, spriteRenderHeight);
    }

    public MovableSprite(int id, boolean blocking, Scene scene, float movingSpeed, int spriteWidth, int spriteHeight) {
        super(id, blocking, scene, spriteWidth, spriteHeight);
        this.movingSpeed = movingSpeed;
    }

    public MovableSprite(int id, boolean blocking, Scene scene, float movingSpeed, int spriteWidth, int spriteHeight, int spriteRenderWidth, int spriteRenderHeight) {
        super(id, blocking, scene, spriteWidth, spriteHeight, spriteRenderWidth, spriteRenderHeight);
        this.movingSpeed = movingSpeed;
    }

    public Status getStatus() {
        return status;
    }
    public float getTargetX() {
        return targetPos.first;
    }
    public float getTargetY() {
        return targetPos.second;
    }
    public FloatPair getTargetPos() {
        return targetPos;
    }

    private IntPair tryMove(Direction direction) {
        int newX = Math.round(posX), newY = Math.round(posY);
        switch (direction) {
            case left:
                newX -= 1;
                break;
            case up:
                newY -= 1;
                break;
            case right:
                newX += 1;
                break;
            case down:
                newY += 1;
                break;
        }
        return new IntPair(newX, newY);
    }

    public boolean doMove(Direction direction) {
        if (status != Status.Ok) {
            return false;
        }
        IntPair newPosition = tryMove(direction);
        if (scene.spriteCanMoveTo(this, newPosition.first, newPosition.second)) {
            status = Status.Moving;
            targetDeltaPos.first = newPosition.first - posX;
            targetDeltaPos.second = newPosition.second - posY;
            targetPos.first = newPosition.first.floatValue();
            targetPos.second = newPosition.second.floatValue();
            movingTime = 0.0f;
            return true;
        }
        return false;
    }

    public boolean canMove(Direction direction) {
        if (status != Status.Ok) {
            return false;
        }
        IntPair newPosition = tryMove(direction);
        if (scene.spriteCanMoveTo(this, newPosition.first, newPosition.second)) {
            return true;
        }
        return false;
    }

    /**
     * This function will help scene to judge if this sprite can move to (targetX, targetY).
     * @param targetX
     * @param targetY
     * @return The collision box of this try.
     */
    public Set<IntPair> tryToMoveCollisionBox(int targetX, int targetY) {
        int left, right, top, bottom;
        Set<IntPair> collisionBox = new TreeSet<>();
        left = (int) Math.round(Math.floor(Math.min(targetX, posX)));
        right = (int) Math.round(Math.ceil(Math.max(targetX, posX))) + spriteWidth - 1;
        top = (int) Math.round(Math.floor(Math.min(targetY, posY)));
        bottom = (int) Math.round(Math.ceil(Math.max(targetY, posY))) + spriteHeight - 1;

        for (int x = Math.max(left, 0); x <= right && x < Config.MapWidth; x++) {
            for (int y = Math.max(top, 0); y <= bottom && y < Config.MapHeight; y++) {
                collisionBox.add(new IntPair(x, y));
            }
        }
        return collisionBox;
    }

    /**
     * This function will help scene to judge if this sprite can move to (targetX, targetY).
     * @param targetX
     * @param targetY
     * @return The collision box of this try.
     */
    public Set<IntPair> tryToAddCollisionBox(int targetX, int targetY) {
        int left, right, top, bottom;
        Set<IntPair> collisionBox = new TreeSet<>();
        left = (int) Math.round(Math.floor(targetX));
        right = (int) Math.round(Math.ceil(targetX)) + spriteWidth - 1;
        top = (int) Math.round(Math.floor(targetY));
        bottom = (int) Math.round(Math.ceil(targetY)) + spriteHeight - 1;

        for (int x = Math.max(left, 0); x <= right && x < Config.MapWidth; x++) {
            for (int y = Math.max(top, 0); y <= bottom && y < Config.MapHeight; y++) {
                collisionBox.add(new IntPair(x, y));
            }
        }
        return collisionBox;
    }

    // Collision box
    @Override
    public Set<IntPair> getCollisionBox() {
        if (status == Status.Ok) {
            return super.getCollisionBox();
        }
        int left = (int) Math.round(Math.floor(Math.min(targetPos.first, posX)));
        int right = (int) Math.round(Math.ceil(Math.max(targetPos.first, posX))) + spriteWidth - 1;
        int top = (int) Math.round(Math.floor(Math.min(targetPos.second, posY)));
        int bottom = (int) Math.round(Math.ceil(Math.max(targetPos.second, posY))) + spriteHeight - 1;
        Set<IntPair> collisionBox = new TreeSet<>();
        for (int x = Math.max(left, 0); x <= right && x < Config.MapWidth; x++) {
            for (int y = Math.max(top, 0); y <= bottom && y < Config.MapHeight; y++) {
                collisionBox.add(scene.getGameMap().getTile(x, y).getIntPair());
            }
        }
        return collisionBox;
    }

    // Rendering box
    @Override
    public Set<IntPair> getRenderingBox() {
        if (status == Status.Ok) {
            return super.getCollisionBox();
        }
        int left = (int) Math.round(Math.floor(Math.min(targetPos.first, posX)));
        int right = (int) Math.round(Math.ceil(Math.max(targetPos.first, posX))) + spriteRenderWidth - 1;
        int top = (int) Math.round(Math.floor(Math.min(targetPos.second, posY)));
        int bottom = (int) Math.round(Math.ceil(Math.max(targetPos.second, posY))) + spriteRenderHeight - 1;
        Set<IntPair> renderingBox = new TreeSet<>();
        for (int x = Math.max(left, 0); x <= right && x < Config.MapWidth; x++) {
            for (int y = Math.max(top, 0); y <= bottom && y < Config.MapHeight; y++) {
                renderingBox.add(scene.getGameMap().getTile(x, y).getIntPair());
            }
        }
        return renderingBox;
    }



    @Override
    public void OnUpdate(float timestep) {
        if (status == Status.Moving) {
            movingTime += timestep;
            if (movingTime >= 1.0f / movingSpeed) {
                status = Status.Ok;
                setPos(targetPos.first, targetPos.second);
            } else {
                setX(posX + targetDeltaPos.first * movingSpeed * timestep);
                setY(posY + targetDeltaPos.second * movingSpeed * timestep);
            }
        }
        
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        
        oStream.write(super.toBytes());

        oStream.write(DataUtils.floatToBytes(movingSpeed));
        oStream.write(DataUtils.floatToBytes(targetDeltaPos.first));
        oStream.write(DataUtils.floatToBytes(targetDeltaPos.second));
        oStream.write(DataUtils.floatToBytes(targetPos.first));
        oStream.write(DataUtils.floatToBytes(targetPos.second));
        oStream.write(DataUtils.floatToBytes(movingTime));
        oStream.write(DataUtils.intToBytes(status.number));

        return oStream.toByteArray();
    }

    @Override
    public void reload(Reloadable reloadable) {
        assert reloadable instanceof MovableSprite;
        MovableSprite sprite = (MovableSprite) reloadable;

        super.reload(sprite);
        this.movingSpeed = sprite.movingSpeed;
        this.targetDeltaPos.first = sprite.targetDeltaPos.first;
        this.targetDeltaPos.second = sprite.targetDeltaPos.second;
        this.targetPos.first = sprite.targetPos.first;
        this.targetPos.second = sprite.targetPos.second;
        this.movingTime = sprite.movingTime;
        this.status = sprite.status;
    }
}
