package com.pandaawake;

import java.awt.Color;

import com.pandaawake.renderer.AsciiFontTile;

public class Config {

    public static final AsciiFontTile TilesFont = AsciiFontTile.GAME_32_32;
    public static final int TileSize = 32;      // A tile is 32*32 size
    public static final int TileFileWidth = 16;  // File: 16*16 tiles
    public static final int TileFileHeight = 16;  // File: 16*16 tiles
    public static final int MapWidth = 10;
    public static final int MapHeight = 10;

    public static final int EmptyTileX = 0;     // Empty tile's coordinate X in the tiles file
    public static final int EmptyTileY = 0;     // Empty tile's coordinate Y in the tiles file

    public static final String TileFilepath = "src/main/resources/tiles.png";

    public static final int FrameRate = 60;

    public static final Color DefaultBackgroundColor = Color.WHITE;

}
