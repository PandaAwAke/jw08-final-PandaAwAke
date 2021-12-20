package com.pandaawake.gourdgame.player;

import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.sprites.PlayableSprite;
import com.pandaawake.gourdgame.utils.DataUtils;
import com.pandaawake.gourdgame.utils.Direction;
import com.pandaawake.gourdgame.utils.ToBytes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class Player implements ToBytes {

    public PlayableSprite sprite;
    public int id = 0;
    public String name = "";

    public String getName() {
        return name;
    }

    public Player(PlayableSprite sprite) {
        this(sprite, 0);
    }

    public Player(PlayableSprite sprite, int id) {
        this(sprite, id, "");
    }

    public Player(PlayableSprite sprite, int id, String name) {
        this.sprite = sprite;
        this.id = id;
        this.name = name;
    }

    public boolean doMove(Direction direction) {
        return sprite.doMove(direction);
    }

    public boolean setBomb() {
        return sprite.setNewBomb();
    }

    public abstract boolean explodeBomb();
    
    public boolean canSetBomb() {
        return sprite.canSetBomb();
    }

    // ---------------------- Callback Functions ----------------------
    public abstract void OnUpdate(float timestep);

    @Override
    public byte[] toBytes() throws IOException {
        // [spriteBytesLen] [spriteBytes] [id (4)] [nameBytesLen] [nameBytes]
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        oStream.write(DataUtils.addLengthHeader(sprite.toBytes()));
        oStream.write(DataUtils.intToBytes(id));
        oStream.write(DataUtils.addLengthHeader(name.getBytes()));
        return oStream.toByteArray();
    }

    public static Player parseBytes(byte[] bytes, Scene scene) throws IOException {
        ByteArrayInputStream iStream = new ByteArrayInputStream(bytes);
        byte[] intBytes = new byte[4];

        iStream.read(intBytes);
        int spriteDataLen = DataUtils.bytesToInt(intBytes);

        byte[] spriteBytes = new byte[spriteDataLen];
        iStream.read(spriteBytes);
        PlayableSprite sprite = PlayableSprite.parseBytes(spriteBytes, scene);

        iStream.read(intBytes);
        int id = DataUtils.bytesToInt(intBytes);

        iStream.read(intBytes);
        int nameDataLen = DataUtils.bytesToInt(intBytes);

        byte[] nameBytes = new byte[nameDataLen];
        iStream.read(nameBytes);
        String name = new String(nameBytes);

        return new OtherPlayer(sprite, id, name);
    }

}
