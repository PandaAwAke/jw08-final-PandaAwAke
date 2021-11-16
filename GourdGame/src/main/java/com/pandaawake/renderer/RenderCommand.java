package com.pandaawake.renderer;

import com.pandaawake.Config;
import com.pandaawake.scene.GameMap;
import com.pandaawake.sprites.Sprite;
import com.pandaawake.tiles.Thing;
import com.pandaawake.tiles.Tile;
import com.pandaawake.utils.FloatPair;
import com.pandaawake.utils.IntPair;
import com.pandaawake.utils.UtilFunctions;

import java.util.Set;

public class RenderCommand {

    private static Renderer renderer;

    public static void Init() {
        Renderer.Init(Config.TilesFont);
        renderer = Renderer.getRenderer();
    }

    public static void drawSprite(Sprite sprite) {
        renderer.addRepaintTilePositions(sprite.getRenderingBox());
        for (int y = 0; y < sprite.getSpriteHeight(); y++) {
            for (int x = 0; x < sprite.getSpriteRenderWidth(); x++) {
                int indexInsideSprite = y * sprite.getSpriteHeight() + x;
                FloatPair tilePosition = new FloatPair(sprite.getX() + x, sprite.getY() + y);
                int glyphIndex = sprite.getGlyphs().get(indexInsideSprite);
                renderer.addAdditionalTile(tilePosition, glyphIndex);
                // TODO: Distinguish Rendering area and Collision area

            }
        }
    }

    public static void drawGameMap(GameMap gameMap) {
        for (int x = 0; x < gameMap.getWidth(); x++) {
            for (int y = 0; y < gameMap.getHeight(); y++) {
                Tile<Thing> tile = gameMap.getTile(x, y);
                Thing thing = tile.getThing();
                if (thing == null) {
                    renderer.setTile(UtilFunctions.PositionInTilesToIndex(Config.EmptyTileX, Config.EmptyTileY), x, y);
                } else {
                    int glyphIndex = thing.getTiles().indexOf(tile);
                    renderer.setTile(thing.getGlyphs().get(glyphIndex), x, y);
                }
            }
        }
    }

    public static void repaintArea(Set<IntPair> area) {
        renderer.addRepaintTilePositions(area);
    }

}
