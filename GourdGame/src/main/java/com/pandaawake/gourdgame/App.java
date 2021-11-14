package com.pandaawake.gourdgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;

import com.pandaawake.calabashbros.World;
import com.pandaawake.screen.Screen;
import com.pandaawake.screen.WorldScreen;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

public class App extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;

    public static final int DIMENSION = 30;

    public App() {
        super();
        terminal = new AsciiPanel(DIMENSION, DIMENSION, AsciiFont.TALRYTH_15_15);
        add(terminal);
        pack();
        screen = new WorldScreen(DIMENSION);
        addKeyListener(this);
        repaint();

    }

    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        App app = new App();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

}
