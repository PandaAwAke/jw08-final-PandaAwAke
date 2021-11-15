package com.pandaawake.gourdgame;

import javax.swing.JFrame;

import com.pandaawake.renderer.Renderer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainWindow extends JFrame implements KeyListener {
    
    private Renderer terminal;
    private Application app;

    public MainWindow(Renderer terminal, Application app) {
        super();
        this.terminal = terminal;
        this.app = app;
        add(terminal);
        pack();
        addKeyListener(this);
        repaint();
    }
    
    @Override
    public void repaint() {
        terminal.clear();
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
