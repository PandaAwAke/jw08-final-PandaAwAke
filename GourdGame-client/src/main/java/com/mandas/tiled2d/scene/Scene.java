package com.mandas.tiled2d.scene;

import java.util.ArrayList;
import java.util.Collection;

public class Scene {

    private ArrayList<Entity> entities = null;

    public Scene() {}
    public Scene(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public void addEntity(Entity entity) {
        synchronized (this) {
            if (entities == null) {
                throw new NullPointerException("entities is null!");
            }
            entities.add(entity);
        }
    }

    public <T extends Entity> void setEntities(Collection<T> entities) {
        synchronized (this) {
            this.entities = new ArrayList<>();
            this.entities.addAll(entities);
        }
    }

    public ArrayList<Entity> getEntities() {
        if (entities == null) {
            throw new NullPointerException("entities is null!");
        }
        return entities;
    }


    public void OnUpdate(float timestep) {
        synchronized (this) {
            if (entities == null) {
                return;
            }
            for (Entity entity : entities) {
                entity.OnUpdate(timestep);
            }
        }
    }

}
