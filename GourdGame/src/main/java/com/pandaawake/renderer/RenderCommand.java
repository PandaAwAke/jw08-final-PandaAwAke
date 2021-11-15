package com.pandaawake.renderer;

import com.pandaawake.Config;
import com.pandaawake.gamemap.GameMap;
import com.pandaawake.gamemap.Scene;
import com.pandaawake.tiles.Thing;
import com.pandaawake.tiles.Tile;
import com.pandaawake.utils.UtilFunctions;

public class RenderCommand {

    private Renderer terminal;

    public RenderCommand(Renderer terminal) {
        this.terminal = terminal;
    }

    public void draw(Scene scene) {
        GameMap gameMap = scene.getGameMap();
        for (int x = 0; x < scene.getGameMap().getWidth(); x++) {
            for (int y = 0; y < scene.getGameMap().getHeight(); y++) {
                // Drawing tiles here, sprites will be drawn in AsciiPanel
                Tile<Thing> tile = gameMap.getTile(x, y);
                Thing thing = tile.getThing();
                if (thing == null) {
                    terminal.write(UtilFunctions.PositionInTilesToChar(Config.EmptyTileX, Config.EmptyTileY), x, y);
                } else {
                    int glyphIndex = thing.getTiles().indexOf(tile);
                    terminal.write(thing.getGlyphs().get(glyphIndex), x, y);
                }

                // TODO: Migrate sprites' rendering here
            }
        }
    }

}
