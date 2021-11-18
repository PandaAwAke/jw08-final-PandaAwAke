package com.pandaawake.renderer;

import com.pandaawake.utils.IntPair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.TreeMap;

public class Texture {

    protected BufferedImage img;
    protected Map<IntPair, BufferedImage> differentSizeImgs;

    public Texture(BufferedImage img) {
        if (img == null) {
            throw new IllegalArgumentException("img cannot be null!");
        }
        this.img = img;
        differentSizeImgs = new TreeMap<>();
    }
    
    public void setImage(BufferedImage img) {
        if (img == null) {
            throw new IllegalArgumentException("img cannot be null!");
        }
        this.img = img;
    }

    public BufferedImage getImage() {
        return img;
    }

    public BufferedImage getImage(int width, int height) {
        if (width == img.getWidth() && height == img.getHeight()) {
            return img;
        }
        IntPair size = new IntPair(width, height);
        if (differentSizeImgs.containsKey(size)) {
            return differentSizeImgs.get(size);
        }

        Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage scaledBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledBufferedImage.createGraphics();
        graphics2D.drawImage(scaledImage, 0, 0, null);
        graphics2D.dispose();

        differentSizeImgs.put(size, scaledBufferedImage);
        return scaledBufferedImage;
    }

    public static Texture getPureColorTexture(int width, int height, Color color) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = img.createGraphics();
        graphics2D.setColor(color);
        graphics2D.fillRect(0, 0, width, height);
        graphics2D.dispose();

        return new Texture(img);
    }

}
