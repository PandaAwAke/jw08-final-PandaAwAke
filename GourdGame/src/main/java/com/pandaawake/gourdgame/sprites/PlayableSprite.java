package com.pandaawake.gourdgame.sprites;

import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.utils.DataUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public abstract class PlayableSprite extends MovableSprite {



    protected int lives;
    protected Set<Bomb> bombs = new HashSet<>();
    protected int maxBombs;

    public PlayableSprite(int id, int lives, int maxBombs, boolean blocking, Scene scene, float movingSpeed, int spriteWidth, int spriteHeight) {
        super(id, blocking, scene, movingSpeed, spriteWidth, spriteHeight);
        this.lives = lives;
        this.maxBombs = maxBombs;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();

        oStream.write(super.toBytes());

        oStream.write(DataUtils.intToBytes(lives));

        return oStream.toByteArray();
    }

    public int getLives() {
        return lives;
    }

    public Set<Bomb> getBombs() {
        return bombs;
    }

    public boolean canSetBomb() {
        return (status == MovableSprite.Status.Ok) && (bombs.size() < maxBombs);
    }

    public boolean setNewBomb() {
        return false;
    }

    public void bombDestroyed(Bomb bomb) {
        bombs.remove(bomb);
    }


    @Override
    public void reload(Reloadable reloadable) {
        assert reloadable instanceof PlayableSprite;
        PlayableSprite sprite = (PlayableSprite) reloadable;

        super.reload(sprite);
        this.lives = sprite.lives;
    }

}
