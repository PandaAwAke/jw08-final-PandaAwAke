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

    public static final AsciiFontTile GAME_FONT_TILE = new AsciiFontTile(Config.TileFilepath, Config.TileSize, Config.TileSize);

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