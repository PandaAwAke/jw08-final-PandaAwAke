package com.pandaawake;

import com.mandas.tiled2d.renderer.TileFileParser;

import java.awt.*;

public class Config {
    // Global settings
    public static final String Version = "v1.1";
    public static final String WindowTitle = "葫芦娃泡泡堂 By Ma Yingshuo, Java Hw05 " + Version;

    // Tile settings
    public static final int TileSize = 50;      // A tile's size

    // Tile File settings
    public static final String TileFilepath = "src/main/resources/tiles.png";
    public static final TileFileParser TileParser = new TileFileParser(TileFilepath, 50, 50, 16, 16, 0, 0);


    // Map settings
    public static final int MapWidth = 15;
    public static final int MapHeight = 15;



    // Game settings
    //      - AI settings
    public static final float DecisionTime = 0.5f;  // The time(seconds) for AI to make a decision
    public static final float SetBombProbability = 0.05f;    // The probability to set a bomb
    public static final float DoNothingProbability = 0.3f;  // The probability of do nothing
    public static final float ChangeDirectionProbability = 0.3f; // When try to move, the probability of changing direction

    //      - Gamerule settings
    public static final float TimeBeforeExploding = 3.0f;   // The time(seconds) before a bomb explodes
    public static final int HumanPlayerBombs = 2;       // Maximum bombs a human player can set in the scene
    public static final int ComputerPlayerBombs = 2;    // Maximum bombs a computer player can set in the scene
    public static final int HumanPlayerLives = 3;
    public static final int ComputerPlayerLives = 2;
    public static final float HumanPlayerMovingSpeed = 4.0f;     // How many tiles will this sprite move in 1 second
    public static final float ComputerPlayerMovingSpeed = 4.0f;     // How many tiles will this sprite move in 1 second
    public static final int BombExtendedRadius = 1;       // Extended radius of the bomb center, describes the exploding area

}
