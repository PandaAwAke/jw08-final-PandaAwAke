package com.mandas.tiled2d.renderer;

import com.mandas.tiled2d.Config;
import com.mandas.tiled2d.scene.*;
import com.mandas.tiled2d.scene.Component;
import com.mandas.tiled2d.utils.FloatPair;
import com.mandas.tiled2d.utils.IntPair;
import com.mandas.tiled2d.utils.Pair;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JPanel;

/**
 * 2D Renderer for the game.
 * 
 * @author Ma Yingshuo
 */
public class Renderer extends JPanel {

    private static Renderer globalRenderer = null;
    /**
     * Initialize the rendering configuration
     *
     * @param widthInTiles The width of map (in tiles count).
     * @param heightInTiles The height of map (in tiles count).
     * @param tileWidth The width of a tile.
     * @param tileHeight The width of a tile.
     * @param scoreboardWidth The width of scoreboard, set to 0 if you do not need this.
     * @param emptyTextureColor If there are unset tiles in the map, set to this pure color.
     */
    public static void Init(int widthInTiles, int heightInTiles, int tileWidth, int tileHeight, int scoreboardWidth, Color emptyTextureColor, Scene scene) {
        globalRenderer = new Renderer(widthInTiles, heightInTiles, tileWidth,
                tileHeight, scoreboardWidth, Texture.getPureColorTexture(tileWidth, tileHeight, emptyTextureColor), scene);
        globalRenderer.Init();
    }
    /**
     * Initialize the rendering configuration
     *
     * @param widthInTiles The width of map (in tiles count).
     * @param heightInTiles The height of map (in tiles count).
     * @param tileWidth The width of a tile.
     * @param tileHeight The width of a tile.
     * @param scoreboardWidth The width of scoreboard, set to 0 if you do not need this.
     * @param emptyTexture If there are unset tiles in the map, set to this texture.
     */
    public static void Init(int widthInTiles, int heightInTiles, int tileWidth, int tileHeight, int scoreboardWidth, Texture emptyTexture, Scene scene) {
        globalRenderer = new Renderer(widthInTiles, heightInTiles, tileWidth,
                tileHeight, scoreboardWidth, emptyTexture, scene);
        globalRenderer.Init();
    }
    public static Renderer getRenderer() {
        if (globalRenderer == null) {
            throw new NullPointerException("Renderer is not initialized!");
        }
        return globalRenderer;
    }

    private BufferedImage offscreenBuffer;
    private Graphics2D offscreenGraphics;
    private int widthInTiles;
    private int heightInTiles;
    private int tileWidth;
    private int tileHeight;

    private Texture[][] tiles;
    private Texture[][] oldTiles;
    private Texture emptyTexture;
    private int scoreboardWidth;
    private int scoreboardLeft, scoreboardTop;
    private Scene scene;

    // Before every rendering, set this variable to clear tiles inside.
    private Set<IntPair> repaintTilePositions;
    // Additional things to render
    private ArrayList<Pair<FloatPair, Texture>> flotingTiles;

    Renderer(int widthInTiles, int heightInTiles, int tileWidth, int tileHeight, int scoreboardWidth, Texture emptyTexture, Scene scene) {
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
        this.scene = scene;
        this.scoreboardLeft = widthInTiles * tileWidth;
        this.scoreboardTop = 0;
        
        Dimension panelSize = new Dimension(tileWidth * widthInTiles + scoreboardWidth, tileHeight * heightInTiles);
        setPreferredSize(panelSize);
        
        offscreenBuffer = new BufferedImage(panelSize.width, panelSize.height, BufferedImage.TYPE_INT_RGB);
        offscreenGraphics = offscreenBuffer.createGraphics();
        
        tiles = new Texture[widthInTiles][heightInTiles];
        oldTiles = new Texture[widthInTiles][heightInTiles];

        repaintTilePositions = new TreeSet<>();
        flotingTiles = new ArrayList<>();

        Init();
    }

    private void Init() {
        synchronized (this) {
            // Set Clear Color First
            offscreenGraphics.setColor(Config.DefaultBackgroundColor);
            offscreenGraphics.fillRect(0, 0, widthInTiles * tileWidth + scoreboardWidth, heightInTiles * tileHeight);
        }
    }

    /**
     * Clear the entire screen to emptyTile.
     *
     */
    public void clear() {
        synchronized (this) {
            for (int i = 0; i < widthInTiles; i++) {
                for (int j = 0; j < heightInTiles; j++) {
                    tiles[i][j] = emptyTexture;
                    oldTiles[i][j] = emptyTexture;
                }
            }
            repaintTilePositions.clear();
            flotingTiles.clear();
            offscreenGraphics.fillRect(0, 0, tileWidth * widthInTiles + scoreboardWidth, tileHeight * heightInTiles);
        }
    }

    /**
     * Write a tile to the specified position. This updates the cursor's
     * position.
     *
     * @param texture   the tile to write
     * @param x         the distance from the left to begin writing from
     * @param y         the distance from the top to begin writing from
     */
    public void setTexture(Texture texture, int x, int y) {
        synchronized (this) {
            if (x < 0 || x >= widthInTiles)
                throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInTiles + ")");

            if (y < 0 || y >= heightInTiles)
                throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInTiles + ")");

            tiles[x][y] = texture;
        }
    }

    /**
     * This function is used for set some positions to be repainted next time, even through tile does not change.
     * @param repaintPositions Positions of tiles to repaint
     */
    public void addRepaintTilePositions(Collection<IntPair> repaintPositions) {
        synchronized (this) {
            this.repaintTilePositions.addAll(repaintPositions);
        }
    }


    /**
     * This function is used for set some additional render tiles.
     * They should be set before every rendering.
     * @param position          Position to render.
     * @param texture           Texture to show.
     */
    public void addFloatingTile(FloatPair position, Texture texture) {
        synchronized (this) {
            this.flotingTiles.add(new Pair<>(position, texture));
        }
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
        drawScoreboardString(startX, startY, string, Config.ScoreboardTextFont);
    }

    /**
     * This function draws some string in scoreboard.
     * @param startX x in scoreboard
     * @param startY y in scoreboard
     * @param string string to draw
     * @param font Font for drawing
     */
    public void drawScoreboardString(int startX, int startY, String string, Font font) {
        offscreenGraphics.setColor(Config.FontColor);
        offscreenGraphics.setFont(font);
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
        offscreenGraphics.drawImage(texture.getImage(tileWidth, tileHeight), leftPixel, topPixel, null);
    }



    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        synchronized (this) {
            if (g == null)
                throw new NullPointerException();

            // 1: Repaint specified positions
            for (IntPair pos : repaintTilePositions) {
                int x = pos.first, y = pos.second;
                offscreenGraphics.setColor(Config.DefaultBackgroundColor);
                offscreenGraphics.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);

                if (tiles[x][y] == null) {
                    tiles[x][y] = emptyTexture;
                }
                BufferedImage img = tiles[x][y].getImage(tileWidth, tileHeight);
                offscreenGraphics.drawImage(img, x * tileWidth, y * tileHeight, null);
            }
            this.repaintTilePositions.clear();

            // 2: Drawing tiles
            for (int x = 0; x < widthInTiles; x++) {
                for (int y = 0; y < heightInTiles; y++) {
                    if (oldTiles[x][y] == tiles[x][y]) {
                        continue;
                    }
                    if (tiles[x][y] == null) {
                        tiles[x][y] = emptyTexture;
                    }
                    offscreenGraphics.setColor(Config.DefaultBackgroundColor);
                    offscreenGraphics.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                    BufferedImage img = tiles[x][y].getImage(tileWidth, tileHeight);
                    offscreenGraphics.drawImage(img, x * tileWidth, y * tileHeight, null);

                    oldTiles[x][y] = tiles[x][y];
                }
            }

            // 3: Paint all entities in Scene
            for (Entity entity : scene.getEntities()) {
                TransformComponent transComponent = (TransformComponent) entity.getComponent(Component.TransformComponentId);
                double translationX = 0, translationY = 0;
                if (transComponent != null) {
                    AffineTransform transform = transComponent.getTransform();
                    offscreenGraphics.setTransform(transform);
                    translationX = transComponent.getTranslateX();
                    translationY = transComponent.getTranslateY();
                }

                TileTextureRenderComponent ttrComponent = (TileTextureRenderComponent) entity.getComponent(Component.TileTextureRenderComponentId);
                if (ttrComponent != null) {
                    for (Pair<FloatPair, Texture> positionAndTex : ttrComponent.getPositionsAndTextures()) {
                        BufferedImage img = positionAndTex.second.getImage(tileWidth, tileHeight);

                        double leftTileIndex = positionAndTex.first.first + translationX;
                        double topTileIndex = positionAndTex.first.second + translationY;

                        int leftPixel = (int) Math.round(leftTileIndex * tileWidth);
                        int topPixel = (int) Math.round(topTileIndex * tileHeight);

                        offscreenGraphics.drawImage(img, leftPixel, topPixel, null);
                        offscreenGraphics.setTransform(new AffineTransform());

                        // Repaint nearby area next time
                        {
                            int left = (int) Math.round(Math.floor(leftTileIndex));
                            int right = (int) Math.round(Math.ceil(leftTileIndex));
                            int top = (int) Math.round(Math.floor(topTileIndex));
                            int bottom = (int) Math.round(Math.ceil(topTileIndex));
                            for (int x = Math.max(left, 0); x <= right && x < widthInTiles; x++) {
                                for (int y = Math.max(top, 0); y <= bottom && y < heightInTiles; y++) {
                                    this.repaintTilePositions.add(new IntPair(x, y));
                                }
                            }
                        }
                    }
                }
            }

            // 4: Drawing additional tiles
            for (int i = 0; i < flotingTiles.size(); i++) {
                FloatPair position = flotingTiles.get(i).first;
                Texture texture = flotingTiles.get(i).second;
                if (texture != null) {
                    BufferedImage img = texture.getImage(tileWidth, tileHeight);
                    int leftPixel = Math.round(position.first * tileWidth);
                    int topPixel = Math.round(position.second * tileHeight);
                    offscreenGraphics.drawImage(img, leftPixel, topPixel, null);
                }

                // Repaint nearby area next time
                {
                    int left = (int) Math.round(Math.floor(position.first));
                    int right = (int) Math.round(Math.ceil(position.first));
                    int top = (int) Math.round(Math.floor(position.second));
                    int bottom = (int) Math.round(Math.ceil(position.second));

                    for (int x = Math.max(left, 0); x <= right && x < widthInTiles; x++) {
                        for (int y = Math.max(top, 0); y <= bottom && y < heightInTiles; y++) {
                            this.repaintTilePositions.add(new IntPair(x, y));
                        }
                    }
                }
            }

            flotingTiles.clear();

            g.drawImage(offscreenBuffer, 0, 0, this);
        }
    }
}