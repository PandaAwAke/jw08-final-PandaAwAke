package com.mandas.tiled2d.scene;

import java.awt.geom.AffineTransform;

public class TransformComponent implements Component {

    @Override
    public int getIdentification() {
        return Component.TransformComponentId;
    }

    private AffineTransform transform;
    private double rotation = 0.0;
    private double rotationAnchorX = 0.0, rotationAnchorY = 0.0;
    private double translationX = 0.0, translationY = 0.0;
    private double scaleX = 1.0, scaleY = 1.0;
    private boolean recalculate = false;

    public TransformComponent() {
        transform = new AffineTransform();
        recalculate = false;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
        recalculate = true;
    }

    public void setRotation(double rotation, double anchorX, double anchorY) {
        this.rotation = rotation;
        this.rotationAnchorX = anchorX;
        this.rotationAnchorY = anchorY;
        recalculate = true;
    }

    public double getRotation() {
        return rotation;
    }

    public void setTranslation(double x, double y) {
        this.translationX = x;
        this.translationY = y;
        recalculate = true;
    }

    public void setTranslationX(double x) {
        this.translationX = x;
        recalculate = true;
    }

    public void setTranslationY(double y) {
        this.translationY = y;
        recalculate = true;
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
        recalculate = true;
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public AffineTransform getTransform() {
        if (recalculate) {
            recalculateTransform();
        }
        return transform;
    }

    private void recalculateTransform() {
        transform = new AffineTransform();
        //transform.translate(translationX, translationY);
        transform.scale(scaleX, scaleY);
        transform.rotate(rotation, rotationAnchorX, rotationAnchorY);
    }


}
