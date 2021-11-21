package com.mandas.tiled2d.scene;

import java.util.HashSet;
import java.util.Set;

public class Entity {

    Set<Component> components;

    public Entity() {
        components = new HashSet<>();

    }

    public void addComponent(Component component) {
        for (Component comp : components) {
            if (comp.getIdentification() == component.getIdentification()) {
                throw new IllegalArgumentException("Tried to add an existing type of component!");
            }
        }
        components.add(component);
    }

    public Component getComponent(int componentIdentification) {
        for (Component comp : components) {
            if (comp.getIdentification() == componentIdentification) {
                return comp;
            }
        }
        return null;
    }

    public TransformComponent getTransformComponent() {
        for (Component comp : components) {
            if (comp instanceof TransformComponent) {
                return (TransformComponent) comp;
            }
        }
        return null;
    }

    public TileTextureRenderComponent getTileTextureRenderComponent() {
        for (Component comp : components) {
            if (comp instanceof TileTextureRenderComponent) {
                return (TileTextureRenderComponent) comp;
            }
        }
        return null;
    }


    public void OnUpdate(float timestep) {}

}
