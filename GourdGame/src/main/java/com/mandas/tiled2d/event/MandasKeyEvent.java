package com.mandas.tiled2d.event;

import java.awt.event.KeyEvent;

public class MandasKeyEvent extends Event {
    protected int keyCode;
    protected char keyChar;
    public MandasKeyEvent(KeyEvent keyEvent) {
        keyCode = keyEvent.getKeyCode();
        keyChar = keyEvent.getKeyChar();
    }

    public int getKeyCode() {
        return keyCode;
    }

    public char getKeyChar() {
        return keyChar;
    }
}
