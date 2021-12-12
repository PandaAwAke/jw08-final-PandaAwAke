package com.mandas.tiled2d.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventDispatcher
{
    static Map<Object, List<Consumer>> callbackFunctions = new HashMap<>();

    // Dispatch JNativeEvents callback functions to registered Mandas Events callback functions
    public static void dispatch(Event e) {
        /* EventTypeNames:
         * WindowOpened, WindowClosing, WindowClosed
         * WindowIconified, WindowDeiconified
         * WindowActivated, WindowDeactivated
         *
         * MouseClicked, MousePressed, MouseReleased
         * MouseMoved, MouseDragged, MouseWheelMoved
         *
         * KeyTyped, KeyPressed, KeyReleased
         */
        Class<? extends Event> eventClass = null;

        switch (e.getEventTypeName()) {
            case "WindowOpened":
                eventClass = WindowEvents.Opened.class;
                break;
            case "WindowClosing":
                eventClass = WindowEvents.Closing.class;
                break;
            case "WindowClosed":
                eventClass = WindowEvents.Closed.class;
                break;
            case "WindowIconified":
                eventClass = WindowEvents.Iconified.class;
                break;
            case "WindowDeiconified":
                eventClass = WindowEvents.Deiconified.class;
                break;
            case "WindowActivated":
                eventClass = WindowEvents.Activated.class;
                break;
            case "WindowDeactivated":
                eventClass = WindowEvents.Deactivated.class;
                break;
            case "MouseClicked":
                eventClass = MouseEvents.Clicked.class;
                break;
            case "MousePressed":
                eventClass = MouseEvents.Pressed.class;
                break;
            case "MouseReleased":
                eventClass = MouseEvents.Released.class;
                break;
            case "MouseMoved":
                eventClass = MouseEvents.Moved.class;
                break;
            case "MouseDragged":
                eventClass = MouseEvents.Dragged.class;
                break;
            case "MouseWheelMoved":
                eventClass = MouseEvents.WheelMoved.class;
                break;
            case "KeyTyped":
                eventClass = KeyEvents.Typed.class;
                break;
            case "KeyPressed":
                eventClass = KeyEvents.Pressed.class;
                break;
            case "KeyReleased":
                eventClass = KeyEvents.Released.class;
                break;
        }
        List<Consumer> matchedCallbackFunctions = callbackFunctions.get(eventClass);
        if (matchedCallbackFunctions == null) {
            return;
        }
        for (Consumer callbackFunc : matchedCallbackFunctions) {
            callbackFunc.accept(eventClass.cast(e));
        }
    }

    public static <T extends Event> void register(Class<T> eventClass, Consumer<T> callbackFunc) {
        if (callbackFunctions.keySet().contains(eventClass)) {
            callbackFunctions.get(eventClass).add(callbackFunc);
        } else {
            List<Consumer> callbackFunctionList = new ArrayList<>();
            callbackFunctionList.add(callbackFunc);
            callbackFunctions.put(eventClass, callbackFunctionList);
        }
    }

}
