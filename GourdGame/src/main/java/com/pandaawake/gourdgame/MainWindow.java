package com.pandaawake.gourdgame;

import javax.swing.JFrame;

import com.pandaawake.renderer.Renderer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainWindow extends JFrame implements KeyListener {
    
    private Renderer renderer;
    private Application app;

    public MainWindow(Renderer renderer, Application app) {
        super();
        this.renderer = renderer;
        this.app = app;
        add(renderer);
        pack();
        addKeyListener(this);
        repaint();
    }
    
    @Override
    public void repaint() {
        renderer.clear();
        app.OnRender();
        super.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        app.OnKeyPressed(e);
        // repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
