package com.pandaawake.gourdgame;

import com.mandas.tiled2d.utils.TileFileParser;
import com.mandas.tiled2d.utils.IntPair;

import java.util.ArrayList;

public class Config {
    // Core settings
    public static final String Version = "v2.0";
    public static final String WindowTitle = "葫芦娃泡泡堂 By MYS, Java Hw05 " + Version;

    public static final boolean ReplayMode = false; // Replay a play by log file
    public static final boolean ServerMode = true; // Open a server


    // Network settings
    public static final String ServerHostName = "localhost";
    public final static int ServerPort = 9093;

    // Tile settings
    public static final int TileSize = 50;      // A tile's size

    // Tile File settings
    public static final String TileFilepath = "src/main/resources/tiles.png";
    public static final TileFileParser TileParser = new TileFileParser(TileFilepath, 50, 50, 16, 16, 0, 0);

    // Render settings
    public static final int ScoreBoardWidth = 150;
    public static final int RenderWidth = 15;
    public static final int RenderHeight = 15;

    // Map settings
    public static final int MapWidth = 20;
    public static final int MapHeight = 15;

    /**
     * 1: Floor
     * 2: Wall
     * 3: Tree
     * 4: TwiceBreakableWall
     */
    public static final String[] level1TileMap = {
            "22222222222222222222",
            "11133321113111211111",
            "11211222211222211211",
            "11241111111111114211",
            "11221122213222112211",
            "11133112311133211333",
            "11221121144432112211",
            "22113344411311122111",
            "11221123444322112211",
            "11113331131221133311",
            "12221122212221112231",
            "32111111111111111123",
            "22142322211222224211",
            "11113211113322113111",
            "22222222222222222222"
    };
    public static final ArrayList<IntPair> level1HumanPlayerPositions = new ArrayList<>();
    public static final ArrayList<IntPair> level1ComputerPlayerPositions = new ArrayList<>();

    static {
        level1HumanPlayerPositions.add(new IntPair(0, 1));

        level1ComputerPlayerPositions.add(new IntPair(0, 13));
        level1ComputerPlayerPositions.add(new IntPair(19, 13));
        level1ComputerPlayerPositions.add(new IntPair(19, 1));
        level1ComputerPlayerPositions.add(new IntPair(9, 3));
    }

    // Player Name settings
    public static final String[] names = { "a", "b", "c", "d", "e", "f", "g", "h", "i" };



    // Game settings
    //      - AI settings
    public static final float DecisionTime = 0.2f;  // The time(seconds) for AI to make a decision
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
