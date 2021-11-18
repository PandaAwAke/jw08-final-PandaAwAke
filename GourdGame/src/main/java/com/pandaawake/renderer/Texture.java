package com.pandaawake.renderer;

import java.awt.image.BufferedImage;

public class Texture {

    protected BufferedImage img;

    public Texture(BufferedImage img) {
        if (img == null) {
            throw new IllegalArgumentException("img cannot be null!");
        }
        this.img = img;
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

}
