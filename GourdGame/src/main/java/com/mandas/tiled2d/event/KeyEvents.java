package com.mandas.tiled2d.event;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class KeyEvents {
    /** EventTypeNames:
     *  KeyTyped, KeyPressed, KeyReleased
     */
    public static class Typed extends KeyEvent {
        public Typed(NativeKeyEvent keyEvent) {
            super(keyEvent);
            eventTypeName = "KeyTyped";
        }
    }

    public static class Pressed extends KeyEvent {
        public Pressed(NativeKeyEvent keyEvent) {
            super(keyEvent);
            eventTypeName = "KeyPressed";
        }
    }

    public static class Released extends KeyEvent {
        public Released(NativeKeyEvent keyEvent) {
            super(keyEvent);
            eventTypeName = "KeyReleased";
        }
    }
}
