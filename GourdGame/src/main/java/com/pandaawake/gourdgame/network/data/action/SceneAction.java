package com.pandaawake.gourdgame.network.data.action;

import com.mandas.tiled2d.core.Log;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.sprites.Sprite;
import com.pandaawake.gourdgame.tiles.Thing;
import com.pandaawake.gourdgame.utils.DataUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class SceneAction extends Action {

    // Scene action numbers
    public static final int ADD_SPRITES = 1;
    public static final int REMOVE_SPRITES = 2;
    public static final int UPDATE_SPRITES = 3;

    public static final int ADD_THINGS = 4;
    public static final int REMOVE_THINGS = 5;
    public static final int UPDATE_THINGS = 6;

    public SceneAction(int senderClientId) {
        super(senderClientId);
    }

    public abstract byte[] toBytes() throws IOException;

    public static SceneAction parseBytes(int senderClientId, ByteArrayInputStream iStream, Scene scene) throws IOException {
        byte[] fourBytes = new byte[4];
        iStream.read(fourBytes);
        int sceneActionId = DataUtils.bytesToInt(fourBytes);
        switch (sceneActionId) {
            case ADD_SPRITES:
                return AddSprites.parseBytes(senderClientId, iStream, scene);
            case REMOVE_SPRITES:
                return RemoveSprites.parseBytes(senderClientId, iStream, scene);
            case UPDATE_SPRITES:
                return UpdateSprites.parseBytes(senderClientId, iStream, scene);

            case ADD_THINGS:
                return AddThings.parseBytes(senderClientId, iStream, scene);
            case REMOVE_THINGS:
                return RemoveThings.parseBytes(senderClientId, iStream, scene);
            case UPDATE_THINGS:
                return UpdateThings.parseBytes(senderClientId, iStream, scene);
            default:
                Log.app().error("SceneAction.parseBytes() : Illegal data format!");
                break;
        }
        return null;
    }

    public static class AddSprites extends SceneAction {

        public Set<Sprite> sprites;

        public AddSprites(int senderClientId, Set<Sprite> sprites) {
            super(senderClientId);
            this.sprites = sprites;
        }
        
        public byte[] toBytes() throws IOException {
            // [spriteCount (4)] [sprite, sprite, ..., sprite]
            // [sprite] = [spriteBytesCount(4)] [spriteBytes (spriteBytesCount)]
            return DataUtils.concatBytes(DataUtils.intToBytes(ADD_SPRITES), DataUtils.collectionToBytes(sprites));
        }

        public static AddSprites parseBytes(int senderClientId, ByteArrayInputStream iStream, Scene scene) throws IOException {
            Set<Sprite> sprites = new HashSet<>();
            for (byte[] infoBytes : Objects.requireNonNull(DataUtils.parseCollectionBytes(iStream))) {
                sprites.add(Sprite.parseBytes(infoBytes, scene));
            }
            return new AddSprites(senderClientId, sprites);
        }

    }

    public static class RemoveSprites extends SceneAction {

        public Set<Sprite> sprites;

        public RemoveSprites(int senderClientId, Set<Sprite> sprites) {
            super(senderClientId);
            this.sprites = sprites;
        }

        public byte[] toBytes() throws IOException {
            // [spriteCount (4)] [sprite, sprite, ..., sprite]
            // [sprite] = [spriteBytesCount(4)] [spriteBytes (spriteBytesCount)]
            return DataUtils.concatBytes(DataUtils.intToBytes(REMOVE_SPRITES), DataUtils.collectionToBytes(sprites));
        }

        public static RemoveSprites parseBytes(int senderClientId, ByteArrayInputStream iStream, Scene scene) throws IOException {
            Set<Sprite> sprites = new HashSet<>();
            for (byte[] infoBytes : Objects.requireNonNull(DataUtils.parseCollectionBytes(iStream))) {
                sprites.add(Sprite.parseBytes(infoBytes, scene));
            }
            return new RemoveSprites(senderClientId, sprites);
        }

    }

    public static class UpdateSprites extends SceneAction {

        public Set<Sprite> sprites;

        public UpdateSprites(int senderClientId, Set<Sprite> sprites) {
            super(senderClientId);
            this.sprites = sprites;
        }

        public byte[] toBytes() throws IOException {
            // [spriteCount (4)] [sprite, sprite, ..., sprite]
            // [sprite] = [spriteBytesCount(4)] [spriteBytes (spriteBytesCount)]
            return DataUtils.concatBytes(DataUtils.intToBytes(UPDATE_SPRITES), DataUtils.collectionToBytes(sprites));
        }

        public static UpdateSprites parseBytes(int senderClientId, ByteArrayInputStream iStream, Scene scene) throws IOException {
            Set<Sprite> sprites = new HashSet<>();
            for (byte[] infoBytes : Objects.requireNonNull(DataUtils.parseCollectionBytes(iStream))) {
                sprites.add(Sprite.parseBytes(infoBytes, scene));
            }
            return new UpdateSprites(senderClientId, sprites);
        }

    }


    public static class AddThings extends SceneAction {

        public Set<Thing> things;

        public AddThings(int senderClientId, Set<Thing> things) {
            super(senderClientId);
            this.things = things;
        }

        public byte[] toBytes() throws IOException {
            // [spriteCount (4)] [sprite, sprite, ..., sprite]
            // [sprite] = [spriteBytesCount(4)] [spriteBytes (spriteBytesCount)]
            return DataUtils.concatBytes(DataUtils.intToBytes(ADD_THINGS), DataUtils.collectionToBytes(things));
        }

        public static AddThings parseBytes(int senderClientId, ByteArrayInputStream iStream, Scene scene) throws IOException {
            Set<Thing> things = new HashSet<>();
            for (byte[] infoBytes : Objects.requireNonNull(DataUtils.parseCollectionBytes(iStream))) {
                things.add(Thing.parseBytes(infoBytes, scene));
            }
            return new AddThings(senderClientId, things);
        }

    }

    public static class RemoveThings extends SceneAction {

        public Set<Thing> things;

        public RemoveThings(int senderClientId, Set<Thing> things) {
            super(senderClientId);
            this.things = things;
        }

        public byte[] toBytes() throws IOException {
            // [spriteCount (4)] [sprite, sprite, ..., sprite]
            // [sprite] = [spriteBytesCount(4)] [spriteBytes (spriteBytesCount)]
            return DataUtils.concatBytes(DataUtils.intToBytes(REMOVE_THINGS), DataUtils.collectionToBytes(things));
        }

        public static RemoveThings parseBytes(int senderClientId, ByteArrayInputStream iStream, Scene scene) throws IOException {
            Set<Thing> things = new HashSet<>();
            for (byte[] infoBytes : Objects.requireNonNull(DataUtils.parseCollectionBytes(iStream))) {
                things.add(Thing.parseBytes(infoBytes, scene));
            }
            return new RemoveThings(senderClientId, things);
        }

    }

    public static class UpdateThings extends SceneAction {

        public Set<Thing> things;

        public UpdateThings(int senderClientId, Set<Thing> things) {
            super(senderClientId);
            this.things = things;
        }

        public byte[] toBytes() throws IOException {
            // [spriteCount (4)] [sprite, sprite, ..., sprite]
            // [sprite] = [spriteBytesCount(4)] [spriteBytes (spriteBytesCount)]
            return DataUtils.concatBytes(DataUtils.intToBytes(UPDATE_THINGS), DataUtils.collectionToBytes(things));
        }

        public static UpdateThings parseBytes(int senderClientId, ByteArrayInputStream iStream, Scene scene) throws IOException {
            Set<Thing> things = new HashSet<>();
            for (byte[] infoBytes : Objects.requireNonNull(DataUtils.parseCollectionBytes(iStream))) {
                things.add(Thing.parseBytes(infoBytes, scene));
            }
            return new UpdateThings(senderClientId, things);
        }

    }




}
