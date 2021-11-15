package com.pandaawake.renderer;

import com.pandaawake.Config;
import com.pandaawake.gamemap.Scene;
import com.pandaawake.sprites.Sprite;
import com.pandaawake.tiles.Thing;
import com.pandaawake.tiles.Tile;
import com.pandaawake.utils.UtilFunctions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * This simulates a code page 437 ASCII terminal display.
 * 
 * @author Trystan Spangler
 */
public class Renderer extends JPanel {
    private static final long serialVersionUID = -4167851861147593092L;

    /**
     * The color black (pure black).
     */
    public static Color black = new Color(0, 0, 0);

    /**
     * The color red.
     */
    public static Color red = new Color(128, 0, 0);

    /**
     * The color green.
     */
    public static Color green = new Color(0, 128, 0);

    /**
     * The color yellow.
     */
    public static Color yellow = new Color(128, 128, 0);

    /**
     * The color blue.
     */
    public static Color blue = new Color(0, 0, 128);

    /**
     * The color magenta.
     */
    public static Color magenta = new Color(128, 0, 128);

    /**
     * The color cyan.
     */
    public static Color cyan = new Color(0, 128, 128);

    /**
     * The color white (light gray).
     */
    public static Color white = new Color(192, 192, 192);

    /**
     * A brighter black (dark gray).
     */
    public static Color brightBlack = new Color(128, 128, 128);

    /**
     * A brighter red.
     */
    public static Color brightRed = new Color(255, 0, 0);

    /**
     * A brighter green.
     */
    public static Color brightGreen = new Color(0, 255, 0);

    /**
     * A brighter yellow.
     */
    public static Color brightYellow = new Color(255, 255, 0);

    /**
     * A brighter blue.
     */
    public static Color brightBlue = new Color(0, 0, 255);

    /**
     * A brighter magenta.
     */
    public static Color brightMagenta = new Color(255, 0, 255);

    /**
     * A brighter cyan.
     */
    public static Color brightCyan = new Color(0, 255, 255);

    /**
     * A brighter white (pure white).
     */
    public static Color brightWhite = new Color(255, 255, 255);

    private Image offscreenBuffer;
    private Graphics offscreenGraphics;
    private int widthInCharacters;
    private int heightInCharacters;
    private int charWidth = 9;
    private int charHeight = 16;
    private String terminalFontFile = "cp437_9x16.png";
    private int cursorX;
    private int cursorY;
    private BufferedImage glyphSprite;
    private BufferedImage[] glyphs;
    private char[][] chars;
    private char[][] oldChars;
    private AsciiFontTile asciiFont;

    private Scene scene;

    /**
     * Gets the height, in pixels, of a character.
     * 
     * @return
     */
    public int getCharHeight() {
        return charHeight;
    }

    /**
     * Gets the width, in pixels, of a character.
     * 
     * @return
     */
    public int getCharWidth() {
        return charWidth;
    }

    /**
     * Gets the height in characters. A standard terminal is 24 characters high.
     * 
     * @return
     */
    public int getHeightInCharacters() {
        return heightInCharacters;
    }

    /**
     * Gets the width in characters. A standard terminal is 80 characters wide.
     * 
     * @return
     */
    public int getWidthInCharacters() {
        return widthInCharacters;
    }

    /**
     * Gets the distance from the left new text will be written to.
     * 
     * @return
     */
    public int getCursorX() {
        return cursorX;
    }

    /**
     * Sets the distance from the left new text will be written to. This should be
     * equal to or greater than 0 and less than the the width in characters.
     * 
     * @param cursorX the distance from the left new text should be written to
     */
    public void setCursorX(int cursorX) {
        if (cursorX < 0 || cursorX >= widthInCharacters)
            throw new IllegalArgumentException(
                    "cursorX " + cursorX + " must be within range [0," + widthInCharacters + ").");

        this.cursorX = cursorX;
    }

    /**
     * Gets the distance from the top new text will be written to.
     * 
     * @return
     */
    public int getCursorY() {
        return cursorY;
    }

    /**
     * Sets the distance from the top new text will be written to. This should be
     * equal to or greater than 0 and less than the the height in characters.
     * 
     * @param cursorY the distance from the top new text should be written to
     */
    public void setCursorY(int cursorY) {
        if (cursorY < 0 || cursorY >= heightInCharacters)
            throw new IllegalArgumentException(
                    "cursorY " + cursorY + " must be within range [0," + heightInCharacters + ").");

        this.cursorY = cursorY;
    }

    /**
     * Sets the x and y position of where new text will be written to. The origin
     * (0,0) is the upper left corner. The x should be equal to or greater than 0
     * and less than the the width in characters. The y should be equal to or
     * greater than 0 and less than the the height in characters.
     * 
     * @param x the distance from the left new text should be written to
     * @param y the distance from the top new text should be written to
     */
    public void setCursorPosition(int x, int y) {
        setCursorX(x);
        setCursorY(y);
    }

    /**
     * Gets the currently selected font
     * 
     * @return
     */
    public AsciiFontTile getAsciiFont() {
        return asciiFont;
    }

    /**
     * Sets the used font. It is advisable to make sure the parent component is
     * properly sized after setting the font as the panel dimensions will most
     * likely change
     * 
     * @param font
     */
    public void setAsciiFont(AsciiFontTile font) {
        if (this.asciiFont == font) {
            return;
        }
        this.asciiFont = font;

        this.charHeight = font.getHeight();
        this.charWidth = font.getWidth();
        this.terminalFontFile = font.getFontFilename();

        Dimension panelSize = new Dimension(charWidth * widthInCharacters, charHeight * heightInCharacters);
        setPreferredSize(panelSize);

        glyphs = new BufferedImage[256];

        offscreenBuffer = new BufferedImage(panelSize.width, panelSize.height, BufferedImage.TYPE_INT_RGB);
        offscreenGraphics = offscreenBuffer.getGraphics();

        loadGlyphs();

        oldChars = new char[widthInCharacters][heightInCharacters];

    }

    /**
     * Class constructor. Default size is 80x24.
     */
    public Renderer(Scene scene) {
        this(80, 24, scene);
    }

    /**
     * Class constructor specifying the width and height in characters.
     * 
     * @param width
     * @param height
     */
    public Renderer(int width, int height, Scene scene) {
        this(width, height, null, scene);
    }

    /**
     * Class constructor specifying the width and height in characters and the
     * AsciiFont
     * 
     * @param width
     * @param height
     * @param font   if passing null, standard font CP437_9x16 will be used
     */
    public Renderer(int width, int height, AsciiFontTile font, Scene scene) {
        super();

        if (width < 1) {
            throw new IllegalArgumentException("width " + width + " must be greater than 0.");
        }

        if (height < 1) {
            throw new IllegalArgumentException("height " + height + " must be greater than 0.");
        }

        widthInCharacters = width;
        heightInCharacters = height;

        chars = new char[widthInCharacters][heightInCharacters];

        if (font == null) {
            font = AsciiFontTile.CP437_9x16;
        }
        setAsciiFont(font);

        // Initialize Scene
        this.scene = scene;

        // Set Clear Color First
        offscreenGraphics.setColor(Color.WHITE);
        offscreenGraphics.fillRect(0, 0, widthInCharacters * charWidth, heightInCharacters * charHeight);

    }


    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        if (g == null)
            throw new NullPointerException();

        Set<Tile<Thing>> repaintTiles = new HashSet<>();

        // Get sprites' rendering boxes, and repaint these tiles again
        for (Sprite sprite : scene.getSprites()) {
            repaintTiles.addAll(sprite.getRenderingBox());
        }

        // TODO: Other repaint tiles from scene?

        // Set to 0, and it will be repainted later
        for (Tile<Thing> tile : repaintTiles) {
            int x = tile.getxPos(), y = tile.getyPos();
            BufferedImage img = glyphs[chars[x][y]];
            offscreenGraphics.setColor(Color.WHITE);
            offscreenGraphics.fillRect(x * charWidth, y * charHeight, charWidth, charHeight);
            offscreenGraphics.drawImage(img, x * charWidth, y * charHeight, null);
        }


        // Drawing tiles, tiles will be set from GameSceneScreen
        for (int x = 0; x < widthInCharacters; x++) {
            for (int y = 0; y < heightInCharacters; y++) {
                if (oldChars[x][y] == chars[x][y])
                    continue;
                BufferedImage img = glyphs[chars[x][y]];
                offscreenGraphics.setColor(Color.WHITE);
                offscreenGraphics.fillRect(x * charWidth, y * charHeight, charWidth, charHeight);
                offscreenGraphics.drawImage(img, x * charWidth, y * charHeight, null);
                oldChars[x][y] = chars[x][y];
            }
        }

        // Drawing sprites, by Ypos and Xpos
        for (Sprite sprite : scene.getSprites()) {
            for (int i = 0; i < sprite.getSpriteRenderWidth(); i++) {
                for (int j = 0; j < sprite.getSpriteHeight(); j++) {
                    int index = i * sprite.getSpriteHeight() + j;
                    BufferedImage img = glyphs[sprite.getGlyphs().get(index)];
                    int leftPixel = Math.round((sprite.getX() + i) * charWidth);
                    int topPixel = Math.round((sprite.getY() + j) * charHeight);
                    // TODO: Distinguish Rendering area and Collision area
                    offscreenGraphics.drawImage(img, leftPixel, topPixel, null);
                }
            }
        }

        g.drawImage(offscreenBuffer, 0, 0, this);
    }

    private void loadGlyphs() {
        try {
            glyphSprite = ImageIO.read(new File(terminalFontFile));
        } catch (IOException e) {
            System.err.println("loadGlyphs(): " + e.getMessage());
        }

        for (int i = 0; i < 256; i++) {
            int sx = (i % 16) * charWidth;
            int sy = (i / 16) * charHeight;

            glyphs[i] = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
            glyphs[i].getGraphics().drawImage(glyphSprite, 0, 0, charWidth, charHeight, sx, sy, sx + charWidth,
                    sy + charHeight, null);
        }
    }

    /**
     * Create a <code>LookupOp</code> object (lookup table) mapping the original
     * pixels to the background and foreground colors, respectively.
     * 
     * @param bgColor the background color
     * @param fgColor the foreground color
     * @return the <code>LookupOp</code> object (lookup table)
     */
    private LookupOp setColors(Color bgColor, Color fgColor) {
        short[] a = new short[256];
        short[] r = new short[256];
        short[] g = new short[256];
        short[] b = new short[256];

        byte bga = (byte) (bgColor.getAlpha());
        byte bgr = (byte) (bgColor.getRed());
        byte bgg = (byte) (bgColor.getGreen());
        byte bgb = (byte) (bgColor.getBlue());

        byte fga = (byte) (fgColor.getAlpha());
        byte fgr = (byte) (fgColor.getRed());
        byte fgg = (byte) (fgColor.getGreen());
        byte fgb = (byte) (fgColor.getBlue());

        for (int i = 0; i < 256; i++) {
            if (i == 0) {
                a[i] = bga;
                r[i] = bgr;
                g[i] = bgg;
                b[i] = bgb;
            } else {
                a[i] = fga;
                r[i] = fgr;
                g[i] = fgg;
                b[i] = fgb;
            }
        }

        short[][] table = { r, g, b, a };
        return new LookupOp(new ShortLookupTable(0, table), null);
    }

    /**
     * Clear the entire screen to whatever the default background color is.
     * 
     * @return this for convenient chaining of method calls
     */
    public Renderer clear() {
        return clear(UtilFunctions.PositionInTilesToChar(Config.EmptyTileX, Config.EmptyTileY), 0, 0, widthInCharacters, heightInCharacters);
    }

    /**
     * Clear the entire screen with the specified character and whatever the default
     * foreground and background colors are.
     * 
     * @param character the character to write
     * @return this for convenient chaining of method calls
     */
    public Renderer clear(char character) {
        if (character < 0 || character >= glyphs.length)
            throw new IllegalArgumentException(
                    "character " + character + " must be within range [0," + glyphs.length + "].");

        return clear(character, 0, 0, widthInCharacters, heightInCharacters);
    }

    /**
     * Clear the section of the screen with the specified character and whatever the
     * default foreground and background colors are. The cursor position will not be
     * modified.
     * 
     * @param character the character to write
     * @param x         the distance from the left to begin writing from
     * @param y         the distance from the top to begin writing from
     * @param width     the height of the section to clear
     * @param height    the width of the section to clear
     * @return this for convenient chaining of method calls
     */
    public Renderer clear(char character, int x, int y, int width, int height) {
        if (character < 0 || character >= glyphs.length)
            throw new IllegalArgumentException(
                    "character " + character + " must be within range [0," + glyphs.length + "].");

        if (x < 0 || x >= widthInCharacters)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")");

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")");

        if (width < 1)
            throw new IllegalArgumentException("width " + width + " must be greater than 0.");

        if (height < 1)
            throw new IllegalArgumentException("height " + height + " must be greater than 0.");

        if (x + width > widthInCharacters)
            throw new IllegalArgumentException(
                    "x + width " + (x + width) + " must be less than " + (widthInCharacters + 1) + ".");

        if (y + height > heightInCharacters)
            throw new IllegalArgumentException(
                    "y + height " + (y + height) + " must be less than " + (heightInCharacters + 1) + ".");

        int originalCursorX = cursorX;
        int originalCursorY = cursorY;
        for (int xo = x; xo < x + width; xo++) {
            for (int yo = y; yo < y + height; yo++) {
                write(character, xo, yo);
            }
        }
        cursorX = originalCursorX;
        cursorY = originalCursorY;

        return this;
    }

    /**
     * Write a character to the cursor's position. This updates the cursor's
     * position.
     * 
     * @param character the character to write
     * @return this for convenient chaining of method calls
     */
    public Renderer write(char character) {
        if (character < 0 || character > glyphs.length)
            throw new IllegalArgumentException(
                    "character " + character + " must be within range [0," + glyphs.length + "].");

        return write(character, cursorX, cursorY);
    }

    /**
     * Write a character to the specified position. This updates the cursor's
     * position.
     *
     * @param character the character to write
     * @param x         the distance from the left to begin writing from
     * @param y         the distance from the top to begin writing from
     * @return this for convenient chaining of method calls
     */
    public Renderer write(char character, int x, int y) {
        if (character < 0 || character >= glyphs.length)
            throw new IllegalArgumentException(
                    "character " + character + " must be within range [0," + glyphs.length + "].");

        if (x < 0 || x >= widthInCharacters)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")");

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")");

        chars[x][y] = character;
        cursorX = x + 1;
        cursorY = y;
        return this;
    }
}