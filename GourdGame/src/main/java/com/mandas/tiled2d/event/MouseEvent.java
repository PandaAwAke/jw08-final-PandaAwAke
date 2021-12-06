package com.mandas.tiled2d.event;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

public class MouseEvent extends Event {
    protected int button;
    protected int x, y;

    public MouseEvent(NativeMouseEvent mouseEvent) {
        button = mouseEvent.getButton();
        x = mouseEvent.getX();
        y = mouseEvent.getY();
    }

    public int getButton() {
        return button;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
