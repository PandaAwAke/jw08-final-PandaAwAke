package com.pandaawake.renderer;

import com.pandaawake.Config;
import com.pandaawake.utils.FloatPair;
import com.pandaawake.utils.IntPair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JPanel;

/**
 * Renderer for the game, based on AsciiPanel.
 * 
 * @author Trystan Spangler && Ma Yingshuo
 */
public class Renderer extends JPanel {

    private static Renderer globalRenderer = null;
    public static void Init(Texture emptyTexture) {
        globalRenderer = new Renderer(Config.MapWidth, Config.MapHeight, Config.TileSize, Config.TileSize, Config.ScoreBoardWidth, emptyTexture);
        globalRenderer.Init();
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

    private Texture[][] tiles;
    private Texture[][] oldTiles;
    private Texture emptyTexture;
    private int scoreboardWidth;
    private int scoreboardLeft, scoreboardTop;

    // Before every rendering, set this variable to clear tiles inside.
    private Set<IntPair> repaintTilePositions;
    // Additional things to render
    private ArrayList<FloatPair> additionalTilePositions;
    private ArrayList<Texture> additionalTileGlyphs;


    /**
     * Class constructor specifying the width and height in gamemap and the
     * tile font
     *
     * @param 
     */
    public Renderer(int widthInTiles, int heightInTiles, int tileWidth, int tileHeight, int scoreboardWidth, Texture emptyTexture) {
        super();
        if (emptyTexture == null) {
            throw new IllegalArgumentException("emptyTexture cannot be null!");
        }
        if (widthInTiles < 1) {
            throw new IllegalArgumentException("width " + widthInTiles + " must be greater than 0.");
        }
        if (heightInTiles < 1) {
            throw new IllegalArgumentException("height " + heightInTiles + " must be greater than 0.");
        }

        this.widthInTiles = widthInTiles;
        this.heightInTiles = heightInTiles;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.emptyTexture = emptyTexture;
        this.scoreboardWidth = scoreboardWidth;
        this.scoreboardLeft = widthInTiles * tileWidth;
        this.scoreboardTop = 0;
        
        Dimension panelSize = new Dimension(tileWidth * widthInTiles + scoreboardWidth, tileHeight * heightInTiles);
        setPreferredSize(panelSize);
        
        offscreenBuffer = new BufferedImage(panelSize.width, panelSize.height, BufferedImage.TYPE_INT_RGB);
        offscreenGraphics = offscreenBuffer.getGraphics();
        
        tiles = new Texture[widthInTiles][heightInTiles];
        oldTiles = new Texture[widthInTiles][heightInTiles];

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
    }

    /**
     * Clear the entire screen to whatever the default background color is.
     * 
     * @return this for convenient chaining of method calls
     */
    public void clear() {
        clear(emptyTexture, 0, 0, widthInTiles, heightInTiles);
    }

    /**
     * Clear the section of the screen with the specified tile and whatever the
     * default foreground and background colors are. The cursor position will not be
     * modified.
     * 
     * @param texture   the texture to clear
     * @param x         the distance from the left to begin writing from
     * @param y         the distance from the top to begin writing from
     * @param width     the height of the section to clear
     * @param height    the width of the section to clear
     * @return this for convenient chaining of method calls
     */
    public void clear(Texture texture, int x, int y, int width, int height) {
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
                setTexture(texture, xo, yo);
            }
        }
    }

    /**
     * Write a tile to the specified position. This updates the cursor's
     * position.
     *
     * @param texture   the tile to write
     * @param x         the distance from the left to begin writing from
     * @param y         the distance from the top to begin writing from
     * @return this for convenient chaining of method calls
     */
    public void setTexture(Texture texture, int x, int y) {
        if (x < 0 || x >= widthInTiles)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInTiles + ")");

        if (y < 0 || y >= heightInTiles)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInTiles + ")");

        tiles[x][y] = texture;
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
     * @param texture
     */
    public void addAdditionalTile(FloatPair position, Texture texture) {
        this.additionalTilePositions.add(position);
        this.additionalTileGlyphs.add(texture);
    }


    public void clearScoreboard() {
        offscreenGraphics.setColor(Config.DefaultBackgroundColor);
        offscreenGraphics.fillRect(scoreboardLeft, scoreboardTop, scoreboardWidth, heightInTiles * tileHeight);
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
     * @param texture texture to draw
     */
    public void drawScoreboardTile(int startX, int startY, Texture texture) {
        int leftPixel = scoreboardLeft + startX;
        int topPixel = scoreboardTop + startY;
        offscreenGraphics.drawImage(texture.getImage(), leftPixel, topPixel, null);
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
            offscreenGraphics.setColor(Config.DefaultBackgroundColor);
            offscreenGraphics.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);

            if (tiles[x][y] != null) {
                BufferedImage img = tiles[x][y].getImage();
                offscreenGraphics.drawImage(img, x * tileWidth, y * tileHeight, null);
            }
        }
        this.repaintTilePositions.clear();

        // Drawing tiles
        for (int x = 0; x < widthInTiles; x++) {
            for (int y = 0; y < heightInTiles; y++) {
                if (oldTiles[x][y] == tiles[x][y]) {
                    continue;
                }
                if (tiles[x][y] != null) {
                    offscreenGraphics.setColor(Config.DefaultBackgroundColor);
                    offscreenGraphics.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                    BufferedImage img = tiles[x][y].getImage();
                    offscreenGraphics.drawImage(img, x * tileWidth, y * tileHeight, null);
                }
                oldTiles[x][y] = tiles[x][y];
            }
        }

        // Drawing additional tiles
        for (int i = 0; i < additionalTileGlyphs.size(); i++) {
            FloatPair position = additionalTilePositions.get(i);
            Texture texture = additionalTileGlyphs.get(i);
            if (texture != null) {
                BufferedImage img = texture.getImage();
                int leftPixel = Math.round(position.first * tileWidth);
                int topPixel = Math.round(position.second * tileHeight);
                offscreenGraphics.drawImage(img, leftPixel, topPixel, null);
            }
        }
        additionalTileGlyphs.clear();
        additionalTilePositions.clear();

        g.drawImage(offscreenBuffer, 0, 0, this);
    }
}