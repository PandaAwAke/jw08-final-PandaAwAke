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
    private static Texture emptyTexture;

    public static void Init() {
        emptyTexture = Config.TileParser.getEmptyTexture();

        Renderer.Init(emptyTexture);
        renderer = Renderer.getRenderer();
    }

    public static void drawSprite(Sprite sprite) {
        renderer.addRepaintTilePositions(sprite.getRenderingBox());
        for (int y = 0; y < sprite.getSpriteRenderHeight(); y++) {
            for (int x = 0; x < sprite.getSpriteRenderWidth(); x++) {
                int indexInsideSprite = y * sprite.getSpriteRenderHeight() + x;
                FloatPair tilePosition = new FloatPair(sprite.getX() + x, sprite.getY() + y);
                Texture texture = sprite.getTextures().get(indexInsideSprite);
                renderer.addAdditionalTile(tilePosition, texture);
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
                    renderer.setTexture(emptyTexture, x, y);
                } else {
                    int glyphIndex = thing.getTiles().indexOf(tile);
                    renderer.setTexture(thing.getTextures().get(glyphIndex), x, y);
                }
            }
        }
    }

    public static void repaintPosition(Set<IntPair> position) {
        renderer.addRepaintTilePositions(position);
    }

    public static void clear() {
        renderer.clear();
    }



    public static void clearScoreboard() {
        renderer.clearScoreboard();
    }

    /**
     * This function draws some string in scoreboard.
     * @param startX x in scoreboard
     * @param startY y in scoreboard
     * @param string string to draw
     */
    public static void drawScoreboardString(int startX, int startY, String string) {
        renderer.drawScoreboardString(startX, startY, string);
    }

    /**
     * This function draws some string in scoreboard.
     * @param startX    x in scoreboard
     * @param startY    y in scoreboard
     * @param texture   texture to draw
     */
    public static void drawScoreboardTile(int startX, int startY, Texture texture) {
        renderer.drawScoreboardTile(startX, startY, texture);
    }

}
