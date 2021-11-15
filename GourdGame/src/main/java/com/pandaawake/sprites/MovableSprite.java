package com.pandaawake.sprites;

import com.pandaawake.gamemap.GameMap;
import com.pandaawake.gamemap.Scene;
import com.pandaawake.utils.Direction;
import com.pandaawake.utils.TwoTuple;

public class MovableSprite extends Sprite {

    protected float movingSpeed = 4.0f;     // How many tiles will this sprite move in 1 second
    protected TwoTuple<Float, Float> targetDeltaPos = new TwoTuple<>(0.0f, 0.0f);
    protected TwoTuple<Integer, Integer> targetPos = new TwoTuple<>(0, 0);
    protected float movingTime = 0.0f;      // How many seconds have this sprite been moving


    public MovableSprite(Scene scene, int spriteWidth, int spriteHeight) {
        super(scene, spriteWidth, spriteHeight);
    }

    public MovableSprite(Scene scene, int spriteWidth, int spriteHeight, int spriteRenderWidth, int spriteRenderHeight) {
        super(scene, spriteWidth, spriteHeight, spriteRenderWidth, spriteRenderHeight);
    }

    public MovableSprite(Scene scene, float movingSpeed, int spriteWidth, int spriteHeight) {
        super(scene, spriteWidth, spriteHeight);
        this.movingSpeed = movingSpeed;
    }

    public MovableSprite(Scene scene, float movingSpeed, int spriteWidth, int spriteHeight, int spriteRenderWidth, int spriteRenderHeight) {
        super(scene, spriteWidth, spriteHeight, spriteRenderWidth, spriteRenderHeight);
        this.movingSpeed = movingSpeed;
    }

    private TwoTuple<Integer, Integer> tryMove(Direction direction) {
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
        return new TwoTuple<Integer, Integer>(newX, newY);
    }

    public boolean doMove(Direction direction) {
        if (status != Status.Ok) {
            return false;
        }
        TwoTuple<Integer, Integer> newPosition = tryMove(direction);
        if (canStay(newPosition.first, newPosition.second)) {
            status = Status.Moving;
            targetDeltaPos.first = newPosition.first - posX;
            targetDeltaPos.second = newPosition.second - posY;
            targetPos.first = newPosition.first;
            targetPos.second = newPosition.second;
            movingTime = 0.0f;
            return true;
        }
        return false;
    }

    private boolean canStay(int x, int y) {
        GameMap gameMap = scene.getGameMap();
        if (!gameMap.insideMap(x, y)) {
            return false;
        }
        if (gameMap.getTile(x, y).getThing() == null) {
            return true;
        }
        return !gameMap.getTile(x, y).getThing().isBlocking();
    }




    @Override
    public void OnUpdate(float timestep) {
        if (status == Status.Moving) {
            this.posX += targetDeltaPos.first * movingSpeed * timestep;
            this.posY += targetDeltaPos.second * movingSpeed * timestep;
            movingTime += timestep;
            if (movingTime >= 1.0f / movingSpeed) {
                status = Status.Ok;
                this.posX = targetPos.first;
                this.posY = targetPos.second;
            }
        }
        
    }
}
