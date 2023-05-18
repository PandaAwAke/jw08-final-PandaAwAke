package com.mandas.tiled2d.scene.component;

import com.mandas.tiled2d.renderer.Camera;

public class CameraComponent implements Component {

    @Override
    public int getIdentification() {
        return Component.CameraComponentId;
    }

    private final Camera camera;
    private boolean renderingCamera = false;

    CameraComponent(Camera camera, boolean renderingCamera) {
        this.camera = camera;
        this.renderingCamera = renderingCamera;
    }

    public CameraComponent(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setRenderingCamera(boolean renderingCamera) {
        this.renderingCamera = renderingCamera;
    }

    public boolean isRenderingCamera() {
        return renderingCamera;
    }


}
