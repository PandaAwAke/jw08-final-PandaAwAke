package com.mandas.tiled2d.scene.component;

import com.mandas.tiled2d.scene.component.Component;

public class TransformComponent implements Component {

    @Override
    public int getIdentification() {
        return Component.TransformComponentId;
    }

    //private double rotation = 0.0;
    //private double rotationAnchorX = 0.0, rotationAnchorY = 0.0;
    private double translationX = 0.0, translationY = 0.0;
    private double scaleX = 1.0, scaleY = 1.0;

    public TransformComponent() {
    }

    // Disabled now
//    private void setRotation(double rotation) {
//        this.rotation = rotation;
//        recalculate = true;
//    }
//
//    private void setRotation(double rotation, double anchorX, double anchorY) {
//        this.rotation = rotation;
//        this.rotationAnchorX = anchorX;
//        this.rotationAnchorY = anchorY;
//        recalculate = true;
//    }
//
//    public double getRotation() {
//        return rotation;
//    }
//
//    public double getRotationAnchorX() {
//        return rotationAnchorX;
//    }
//
//    public double getRotationAnchorY() {
//        return rotationAnchorY;
//    }

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
