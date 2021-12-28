package com.mandas.tiled2d.event;

import java.awt.event.KeyEvent;

public class KeyEvents {
    /** EventTypeNames:
     *  KeyTyped, KeyPressed, KeyReleased
     */
    public static class Typed extends MandasKeyEvent {
        public Typed(KeyEvent keyEvent) {
            super(keyEvent);
            eventTypeName = "KeyTyped";
        }
    }

    public static class Pressed extends MandasKeyEvent {
        public Pressed(KeyEvent keyEvent) {
            super(keyEvent);
            eventTypeName = "KeyPressed";
        }
    }

    public static class Released extends MandasKeyEvent {
        public Released(KeyEvent keyEvent) {
            super(keyEvent);
            eventTypeName = "KeyReleased";
        }
    }
}
