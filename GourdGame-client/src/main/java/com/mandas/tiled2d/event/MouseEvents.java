package com.mandas.tiled2d.event;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelEvent;

public class MouseEvents {
    /** EventTypeNames:
     *  MouseClicked, MousePressed, MouseReleased
     *  MouseMoved, MouseDragged, MouseWheelMoved
     */
    public static class Clicked extends MouseEvent {
        public Clicked(NativeMouseEvent mouseEvent) {
            super(mouseEvent);
            eventTypeName = "MouseClicked";
        }
    }

    public static class Pressed extends MouseEvent {
        public Pressed(NativeMouseEvent mouseEvent) {
            super(mouseEvent);
            eventTypeName = "MousePressed";
        }
    }

    public static class Released extends MouseEvent {
        public Released(NativeMouseEvent mouseEvent) {
            super(mouseEvent);
            eventTypeName = "MouseReleased";
        }
    }

    public static class Moved extends MouseEvent {
        public Moved(NativeMouseEvent mouseEvent) {
            super(mouseEvent);
            eventTypeName = "MouseMoved";
        }
    }

    public static class Dragged extends MouseEvent {
        public Dragged(NativeMouseEvent mouseEvent) {
            super(mouseEvent);
            eventTypeName = "MouseDragged";
        }
    }

    public static class WheelMoved extends MouseEvent {
        protected int wheelRotation;
        protected int wheelDirection;
        public WheelMoved(NativeMouseWheelEvent mouseWheelEvent) {
            super(mouseWheelEvent);
            wheelDirection = mouseWheelEvent.getWheelDirection();
            wheelRotation = mouseWheelEvent.getWheelRotation();
            eventTypeName = "MouseWheelMoved";
        }

        // Negative: up, Positive: down
        public int getWheelRotation() {
            return wheelRotation;
        }

        public int getWheelDirection() {
            return wheelDirection;
        }
    }
}
