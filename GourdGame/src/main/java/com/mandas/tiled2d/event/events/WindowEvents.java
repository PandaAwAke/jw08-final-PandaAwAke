package com.mandas.tiled2d.event.events;

import com.mandas.tiled2d.event.Event;

public class WindowEvents {
    /** EventTypeNames:
     *  WindowOpened, WindowClosing, WindowClosed
     *  WindowIconified, WindowDeiconified
     *  WindowActivated, WindowDeactivated
     */
    public static class Opened extends Event {
        public Opened() {
            eventTypeName = "WindowOpened";
        }
    }
    public static class Closing extends Event {
        public Closing() {
            eventTypeName = "WindowClosing";
        }
    }
    public static class Closed extends Event {
        public Closed() {
            eventTypeName = "WindowClosed";
        }
    }
    public static class Iconified extends Event {
        public Iconified() {
            eventTypeName = "WindowIconified";
        }
    }
    public static class Deiconified extends Event {
        public Deiconified() {
            eventTypeName = "WindowDeiconified";
        }
    }
    public static class Activated extends Event {
        public Activated() {
            eventTypeName = "WindowActivated";
        }
    }
    public static class Deactivated extends Event {
        public Deactivated() {
            eventTypeName = "WindowDeactivated";
        }
    }
}
