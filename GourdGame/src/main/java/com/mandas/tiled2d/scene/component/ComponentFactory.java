package com.mandas.tiled2d.scene.component;

import com.mandas.tiled2d.renderer.Camera;

public class ComponentFactory {

    public static Component createComponent(Class<? extends Component> componentClass, Object ... args) {
        if (componentClass.equals(AudioComponent.class)) {
            // not implemented
            assert false;
            return null;
        } else if (componentClass.equals(CameraComponent.class)) {
            assert args.length >= 2;
            assert args[0] instanceof Camera;
            assert args[1] instanceof Boolean;
            return new CameraComponent((Camera) args[0], (Boolean) args[1]);
        } else if (componentClass.equals(TileTextureRenderComponent.class)) {
            return new TileTextureRenderComponent();
        } else if (componentClass.equals(TransformComponent.class)) {
            return new TransformComponent();
        }
        assert false;
        return null;
    }

}
