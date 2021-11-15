package com.pandaawake.sprites;

import com.pandaawake.gamemap.Scene;

public class Player extends MovableSprite {

    public Player(Scene scene, int spriteWidth, int spriteHeight) {
        super(scene, spriteWidth, spriteHeight);
    }

    public Player(Scene scene, int spriteWidth, int spriteHeight, int spriteRenderWidth, int spriteRenderHeight) {
        super(scene, spriteWidth, spriteHeight, spriteRenderWidth, spriteRenderHeight);
    }

    public Player(Scene scene, float moveSpeed, int spriteWidth, int spriteHeight) {
        super(scene, moveSpeed, spriteWidth, spriteHeight);
    }

    public Player(Scene scene, float moveSpeed, int spriteWidth, int spriteHeight, int spriteRenderWidth, int spriteRenderHeight) {
        super(scene, moveSpeed, spriteWidth, spriteHeight, spriteRenderWidth, spriteRenderHeight);
    }

}
