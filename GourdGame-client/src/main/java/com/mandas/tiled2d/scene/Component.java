package com.mandas.tiled2d.scene;

public interface Component {

    public static final int TileTextureRenderComponentId = 0;
    public static final int TransformComponentId = 1;
    public static final int AudioComponentId = 2;
    public static final int CameraComponentId = 3;

    /**
     * Every Component subclass should has a unique identification.
     * @return identification
     */
    public int getIdentification();

}
