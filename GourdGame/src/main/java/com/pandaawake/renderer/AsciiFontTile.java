package com.pandaawake.renderer;

import com.pandaawake.Config;

/**
 * This class holds provides all available Fonts for the AsciiPanel. Some
 * graphics are from the Dwarf Fortress Tileset Wiki Page
 * 
 * @author zn80
 *
 */
public class AsciiFontTile {

    public static final AsciiFontTile CP437_8x8 = new AsciiFontTile("src/main/resources/cp437_8x8.png", 8, 8);
    public static final AsciiFontTile CP437_10x10 = new AsciiFontTile("src/main/resources/cp437_10x10.png", 10, 10);
    public static final AsciiFontTile CP437_12x12 = new AsciiFontTile("src/main/resources/cp437_12x12.png", 12, 12);
    public static final AsciiFontTile CP437_16x16 = new AsciiFontTile("src/main/resources/cp437_16x16.png", 16, 16);
    public static final AsciiFontTile CP437_9x16 = new AsciiFontTile("src/main/resources/cp437_9x16.png", 9, 16);
    public static final AsciiFontTile DRAKE_10x10 = new AsciiFontTile("src/main/resources/drake_10x10.png", 10, 10);
    public static final AsciiFontTile TAFFER_10x10 = new AsciiFontTile("src/main/resources/taffer_10x10.png", 10, 10);
    public static final AsciiFontTile QBICFEET_10x10 = new AsciiFontTile("src/main/resources/qbicfeet_10x10.png", 10, 10);
    public static final AsciiFontTile TALRYTH_15_15 = new AsciiFontTile("src/main/resources/talryth_square_15x15.png", 15, 15);
    public static final AsciiFontTile GAME_32_32 = new AsciiFontTile(Config.TileFilepath, 32, 32);

    private String fontFilename;

    public String getFontFilename() {
        return fontFilename;
    }

    private int width;

    public int getWidth() {
        return width;
    }

    private int height;

    public int getHeight() {
        return height;
    }

    public AsciiFontTile(String filename, int width, int height) {
        this.fontFilename = filename;
        this.width = width;
        this.height = height;
    }
}