package com.mandas.tiled2d.renderer;

public class Camera {

    private double widthInTiles, heightInTiles;
    private double translationX = 0.0, translationY = 0.0;
    private double scaleX = 1.0, scaleY = 1.0;

    public Camera(double widthInTiles, double heightInTiles) {
        this.widthInTiles = widthInTiles;
        this.heightInTiles = heightInTiles;
    }

    public double getWidthInTiles() {
        return widthInTiles;
    }

    public double getHeightInTiles() {
        return heightInTiles;
    }

    public void setTranslation(double x, double y) {
        this.translationX = x;
        this.translationY = y;
    }

    public void setTranslationX(double x) {
        this.translationX = x;
    }

    public void setTranslationY(double y) {
        this.translationY = y;
    }

    public double getTranslateX() {
        return translationX;
    }

    public double getTranslateY() {
        return translationY;
    }

    public void setScale(double x, double y) {
        this.scaleX = x;
        this.scaleY = y;
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }


}
