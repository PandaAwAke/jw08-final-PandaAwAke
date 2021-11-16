package com.pandaawake.renderer;

import com.pandaawake.Config;
import com.pandaawake.utils.FloatPair;
import com.pandaawake.utils.IntPair;
import com.pandaawake.utils.UtilFunctions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Renderer for the game, based on AsciiPanel.
 * 
 * @author Trystan Spangler && Ma Yingshuo
 */
public class Renderer extends JPanel {

    private static Renderer globalRenderer = null;
    public static void Init(AsciiFontTile tilefont) {
        globalRenderer = new Renderer(tilefont);
    }
    public static Renderer getRenderer() {
        if (globalRenderer == null) {
            throw new NullPointerException("Renderer is not initialized!");
        }
        return globalRenderer;
    }

    private Image offscreenBuffer;
    private Graphics offscreenGraphics;
    private int widthInTiles;
    private int heightInTiles;
    private int tileWidth = Config.TileFileWidth;
    private int tileHeight = Config.TileFileHeight;
    private String terminalFontFile = Config.TileFilepath;
    private AsciiFontTile fonttile;
    private BufferedImage glyphSprite;

    private BufferedImage[] glyphs;
    private int[][] tiles;
    private int[][] oldTiles;

    // Before every rendering, set this variable to clear tiles inside.
    private Set<IntPair> repaintTilePositions;
    // Additional things to render
    private ArrayList<FloatPair> additionalTilePositions;
    private ArrayList<Integer> additionalTileGlyphs;

    private int scoreboardLeft, scoreboardTop;

    /**
     * Class constructor specifying the width and height in gamemap and the
     * tile font
     *
     * @param tilefont   if passing null, standard font CP437_9x16 will be used
     */
    public Renderer(AsciiFontTile tilefont) {
        super();

        int width = Config.MapWidth;
        int height = Config.MapHeight;

        if (width < 1) {
            throw new IllegalArgumentException("width " + width + " must be greater than 0.");
        }

        if (height < 1) {
            throw new IllegalArgumentException("height " + height + " must be greater than 0.");
        }

        widthInTiles = width;
        heightInTiles = height;

        tiles = new int[widthInTiles][heightInTiles];

        if (tilefont == null) {
            throw new IllegalArgumentException("Tilefont is null!");
        }
        setAsciiFontTile(tilefont);

        repaintTilePositions = new TreeSet<>();
        additionalTilePositions = new ArrayList<>();
        additionalTileGlyphs = new ArrayList<>();
    }

    public void Init() {
        // Set Clear Color First
        offscreenGraphics.setColor(Config.DefaultBackgroundColor);
        offscreenGraphics.fillRect(0, 0, widthInTiles * tileWidth + Config.ScoreBoardWidth, heightInTiles * tileHeight);

        // Init scoreboard
        offscreenGraphics.setFont(Config.ScoreboardTextFont);

        scoreboardLeft = widthInTiles * tileWidth;
        scoreboardTop = 0;
    }

    /**
     * Gets the currently selected font
     * 
     * @return
     */
    public AsciiFontTile getAsciiFontTile() {
        return fonttile;
    }
    /**
     * Sets the used font. It is advisable to make sure the parent component is
     * properly sized after setting the font as the panel dimensions will most
     * likely change
     * 
     * @param fonttile
     */
    public void setAsciiFontTile(AsciiFontTile fonttile) {
        if (this.fonttile == fonttile) {
            return;
        }
        this.fonttile = fonttile;

        this.tileHeight = fonttile.getHeight();
        this.tileWidth = fonttile.getWidth();
        this.terminalFontFile = fonttile.getFontFilename();

        Dimension panelSize = new Dimension(tileWidth * widthInTiles + Config.ScoreBoardWidth, tileHeight * heightInTiles);
        setPreferredSize(panelSize);

        glyphs = new BufferedImage[Config.TileFileWidth * Config.TileFileHeight];

        offscreenBuffer = new BufferedImage(panelSize.width, panelSize.height, BufferedImage.TYPE_INT_RGB);
        offscreenGraphics = offscreenBuffer.getGraphics();

        loadGlyphs();

        oldTiles = new int[widthInTiles][heightInTiles];

    }

    private void loadGlyphs() {
        try {
            glyphSprite = ImageIO.read(new File(terminalFontFile));
        } catch (IOException e) {
            System.err.println("loadGlyphs(): " + e.getMessage());
        }

        for (int i = 0; i < Config.TileFileWidth * Config.TileFileHeight; i++) {
            int sx = (i % Config.TileFileWidth) * tileWidth;
            int sy = (i / Config.TileFileWidth) * tileHeight;

            glyphs[i] = new BufferedImage(tileWidth, tileHeight, BufferedImage.TYPE_INT_ARGB);
            glyphs[i].getGraphics().drawImage(glyphSprite, 0, 0, tileWidth, tileHeight, sx, sy, sx + tileWidth,
                    sy + tileHeight, null);
        }
    }

    /**
     * Clear the entire screen to whatever the default background color is.
     * 
     * @return this for convenient chaining of method calls
     */
    public void clear() {
        clear(UtilFunctions.PositionInTilesToIndex(Config.EmptyTileX, Config.EmptyTileY), 0, 0, widthInTiles, heightInTiles);
    }

    /**
     * Clear the section of the screen with the specified tile and whatever the
     * default foreground and background colors are. The cursor position will not be
     * modified.
     * 
     * @param glyphIndex the tile to write
     * @param x         the distance from the left to begin writing from
     * @param y         the distance from the top to begin writing from
     * @param width     the height of the section to clear
     * @param height    the width of the section to clear
     * @return this for convenient chaining of method calls
     */
    public void clear(int glyphIndex, int x, int y, int width, int height) {
        if (glyphIndex < 0 || glyphIndex >= glyphs.length)
            throw new IllegalArgumentException(
                    "tile " + glyphIndex + " must be within range [0," + glyphs.length + "].");

        if (x < 0 || x >= widthInTiles)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInTiles + ")");

        if (y < 0 || y >= heightInTiles)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInTiles + ")");

        if (width < 1)
            throw new IllegalArgumentException("width " + width + " must be greater than 0.");

        if (height < 1)
            throw new IllegalArgumentException("height " + height + " must be greater than 0.");

        if (x + width > widthInTiles)
            throw new IllegalArgumentException(
                    "x + width " + (x + width) + " must be less than " + (widthInTiles + 1) + ".");

        if (y + height > heightInTiles)
            throw new IllegalArgumentException(
                    "y + height " + (y + height) + " must be less than " + (heightInTiles + 1) + ".");
        for (int xo = x; xo < x + width; xo++) {
            for (int yo = y; yo < y + height; yo++) {
                setTile(glyphIndex, xo, yo);
            }
        }
    }

    /**
     * Write a tile to the specified position. This updates the cursor's
     * position.
     *
     * @param glyphIndex the tile to write
     * @param x         the distance from the left to begin writing from
     * @param y         the distance from the top to begin writing from
     * @return this for convenient chaining of method calls
     */
    public void setTile(int glyphIndex, int x, int y) {
        if (glyphIndex < 0 || glyphIndex >= glyphs.length)
            throw new IllegalArgumentException(
                    "tile " + glyphIndex + " must be within range [0," + glyphs.length + "].");

        if (x < 0 || x >= widthInTiles)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInTiles + ")");

        if (y < 0 || y >= heightInTiles)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInTiles + ")");

        tiles[x][y] = glyphIndex;
    }

    /**
     * This function is used for set some positions to be repainted next time, even through tile does not change.
     * @param repaintPositions
     */
    public void addRepaintTilePositions(Collection<IntPair> repaintPositions) {
        this.repaintTilePositions.addAll(repaintPositions);
    }
    
    /**
     * This function is used for set some additional render tiles.
     * They should be set before every rendering.
     * @param position
     * @param glyphIndex
     */
    public void addAdditionalTile(FloatPair position, int glyphIndex) {
        this.additionalTilePositions.add(position);
        this.additionalTileGlyphs.add(glyphIndex);
    }


    public void clearScoreboard() {
        offscreenGraphics.setColor(Config.DefaultBackgroundColor);
        offscreenGraphics.fillRect(scoreboardLeft, scoreboardTop, Config.ScoreBoardWidth, heightInTiles * tileHeight);
    }

    /**
     * This function draws some string in scoreboard.
     * @param startX x in scoreboard
     * @param startY y in scoreboard
     * @param string string to draw
     */
    public void drawScoreboardString(int startX, int startY, String string) {
        offscreenGraphics.setColor(Config.FontColor);
        offscreenGraphics.drawString(string, scoreboardLeft + startX, scoreboardTop + startY);
    }

    /**
     * This function draws some string in scoreboard.
     * @param startX x in scoreboard
     * @param startY y in scoreboard
     * @param glyphIndex tile index to draw
     */
    public void drawScoreboardTile(int startX, int startY, int glyphIndex) {
        BufferedImage img = glyphs[glyphIndex];
        int leftPixel = scoreboardLeft + startX;
        int topPixel = scoreboardTop + startY;
        offscreenGraphics.drawImage(img, leftPixel, topPixel, null);
    }



    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        if (g == null)
            throw new NullPointerException();

        // Repaint specified positions
        for (IntPair pos : repaintTilePositions) {
            int x = pos.first, y = pos.second;
            BufferedImage img = glyphs[tiles[x][y]];
            offscreenGraphics.setColor(Config.DefaultBackgroundColor);
            offscreenGraphics.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
            offscreenGraphics.drawImage(img, x * tileWidth, y * tileHeight, null);
        }
        this.repaintTilePositions.clear();

        // Drawing tiles
        for (int x = 0; x < widthInTiles; x++) {
            for (int y = 0; y < heightInTiles; y++) {
                if (oldTiles[x][y] == tiles[x][y])
                    continue;
                BufferedImage img = glyphs[tiles[x][y]];
                offscreenGraphics.setColor(Config.DefaultBackgroundColor);
                offscreenGraphics.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                offscreenGraphics.drawImage(img, x * tileWidth, y * tileHeight, null);
                oldTiles[x][y] = tiles[x][y];
            }
        }

        // Drawing additional tiles
        for (int i = 0; i < additionalTileGlyphs.size(); i++) {
            FloatPair position = additionalTilePositions.get(i);
            int glyphIndex = additionalTileGlyphs.get(i);
            BufferedImage img = glyphs[glyphIndex];
            int leftPixel = Math.round(position.first * tileWidth);
            int topPixel = Math.round(position.second * tileHeight);
            offscreenGraphics.drawImage(img, leftPixel, topPixel, null);
        }
        additionalTileGlyphs.clear();
        additionalTilePositions.clear();

        g.drawImage(offscreenBuffer, 0, 0, this);
    }
}