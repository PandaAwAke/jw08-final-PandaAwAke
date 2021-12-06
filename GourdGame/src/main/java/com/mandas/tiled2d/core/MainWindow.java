package com.mandas.tiled2d.core;

import javax.swing.JFrame;

import com.mandas.tiled2d.event.EventHandler;
import com.mandas.tiled2d.renderer.Renderer;

public class MainWindow extends JFrame {
    
    private Application app;
    private EventHandler eventHandler;

    public MainWindow(String title, Renderer renderer, Application app) {
        super(title);
        this.app = app;
        this.eventHandler = new EventHandler();
        add(renderer);
        pack();
        addWindowListener(eventHandler);
        repaint();
    }
    
    @Override
    public void repaint() {
        app.OnRender();
        super.repaint();
    }

}
