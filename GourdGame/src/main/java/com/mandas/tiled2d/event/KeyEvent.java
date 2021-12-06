package com.mandas.tiled2d.event;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class KeyEvent extends Event {
    protected int keyCode;
    protected char keyChar;
    public KeyEvent(NativeKeyEvent keyEvent) {
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
