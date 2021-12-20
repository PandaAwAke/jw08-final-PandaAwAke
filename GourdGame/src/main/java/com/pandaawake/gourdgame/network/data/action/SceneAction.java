package com.pandaawake.gourdgame.network.data.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.sprites.Sprite;
import com.pandaawake.gourdgame.utils.DataUtils;

public abstract class SceneAction extends Action {

    public SceneAction(int senderClientId) {
        super(senderClientId);
    }


    public static class AddSprites extends SceneAction {

        private Set<Sprite> sprites;

        public AddSprites(int senderClientId, Set<Sprite> sprites) {
            super(senderClientId);
            this.sprites = sprites;
        }
        
        public byte[] toBytes() throws IOException {
            // [spriteCount (4)] [sprite, sprite, ..., sprite]
            // [sprite] = [spriteBytesCount(4)] [spriteBytes (spriteBytesCount)]
            return DataUtils.collectionToBytes(sprites);
        }
        

    }
    
}
