package com.pandaawake;

import java.awt.Color;

import com.pandaawake.renderer.AsciiFontTile;

public class Config {
    // Tile settings
    public static final AsciiFontTile TilesFont = AsciiFontTile.GAME_FONT_TILE;
    public static final int TileSize = 50;      // A tile's size
    public static final int TileFileWidth = 16;  // File: 16*16 tiles
    public static final int TileFileHeight = 16;  // File: 16*16 tiles
    public static final int EmptyTileX = 0;     // Empty tile's coordinate X in the tiles file
    public static final int EmptyTileY = 0;     // Empty tile's coordinate Y in the tiles file
    public static final String TileFilepath = "src/main/resources/tiles.png";
    public static final Color DefaultBackgroundColor = Color.WHITE;

    // Map settings
    public static final int MapWidth = 15;
    public static final int MapHeight = 15;

    // Core settings
    public static final int MaxFrameRate = 60;

    // Game settings
    //      - AI settings
    public static final float DecisionTime = 0.5f;  // The time(seconds) for AI to make a decision
    public static final float SetBombProbability = 0.05f;    // The probability to set a bomb
    public static final float DoNothingProbability = 0.2f;  // The probability of do nothing
    public static final float ChangeDirectionProbability = 0.3f; // When try to move, the probability of changing direction

    //      - Other settings
    public static final float TimeBeforeExploding = 2.5f;   // The time(seconds) before a bomb explodes

}
