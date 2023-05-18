package com.mandas.tiled2d.event;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.dispatcher.SwingDispatchService;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelListener;
import com.mandas.tiled2d.core.Log;
import com.mandas.tiled2d.event.events.KeyEvents;
import com.mandas.tiled2d.event.events.MouseEvents;
import com.mandas.tiled2d.event.events.WindowEvents;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class EventHandler implements
        NativeMouseInputListener, NativeMouseWheelListener,
        WindowListener, KeyListener
{
    public EventHandler() {
        try {
            GlobalScreen.setEventDispatcher(new SwingDispatchService());
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            Log.mandas().fatal(this.getClass().getName() + ": There was a problem registering the native hook.");
            Log.mandas().fatal(this.getClass().getName() + ": " + ex.getMessage());

            
        }
//        GlobalScreen.addNativeKeyListener(this);  // Key Listener is in MainWindow
        GlobalScreen.addNativeMouseListener(this);
        GlobalScreen.addNativeMouseMotionListener(this);
        GlobalScreen.addNativeMouseWheelListener(this);
    }

    /**
     * Invoked the first time a window is made visible.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowOpened(WindowEvent e) {
        EventDispatcher.dispatch(new WindowEvents.Opened());
    }

    /**
     * Invoked when the user attempts to close the window
     * from the window's system menu.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowClosing(WindowEvent e) {
        EventDispatcher.dispatch(new WindowEvents.Closing());
    }

    /**
     * Invoked when a window has been closed as the result
     * of calling dispose on the window.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowClosed(WindowEvent e) {
        EventDispatcher.dispatch(new WindowEvents.Closed());
    }

    /**
     * Invoked when a window is changed from a normal to a
     * minimized state. For many platforms, a minimized window
     * is displayed as the icon specified in the window's
     * iconImage property.
     *
     * @param e the event to be processed
     * @see Frame#setIconImage
     */
    @Override
    public void windowIconified(WindowEvent e) {
        EventDispatcher.dispatch(new WindowEvents.Iconified());
    }

    /**
     * Invoked when a window is changed from a minimized
     * to a normal state.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowDeiconified(WindowEvent e) {
        EventDispatcher.dispatch(new WindowEvents.Deiconified());
    }

    /**
     * Invoked when the Window is set to be the active Window. Only a Frame or
     * a Dialog can be the active Window. The native windowing system may
     * denote the active Window or its children with special decorations, such
     * as a highlighted title bar. The active Window is always either the
     * focused Window, or the first Frame or Dialog that is an owner of the
     * focused Window.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowActivated(WindowEvent e) {
        EventDispatcher.dispatch(new WindowEvents.Activated());
    }

    /**
     * Invoked when a Window is no longer the active Window. Only a Frame or a
     * Dialog can be the active Window. The native windowing system may denote
     * the active Window or its children with special decorations, such as a
     * highlighted title bar. The active Window is always either the focused
     * Window, or the first Frame or Dialog that is an owner of the focused
     * Window.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowDeactivated(WindowEvent e) {
        EventDispatcher.dispatch(new WindowEvents.Deactivated());
    }

    // @Override
    // public void nativeKeyTyped(NativeKeyEvent e) {
    //     EventDispatcher.dispatch(new KeyEvents.Typed(e));
    // }

    // @Override
    // public void nativeKeyPressed(NativeKeyEvent e) {
    //     EventDispatcher.dispatch(new KeyEvents.Pressed(e));
    // }

    // @Override
    // public void nativeKeyReleased(NativeKeyEvent e) {
    //     EventDispatcher.dispatch(new KeyEvents.Released(e));
    // }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        EventDispatcher.dispatch(new MouseEvents.Clicked(e));
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        EventDispatcher.dispatch(new MouseEvents.Pressed(e));
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        EventDispatcher.dispatch(new MouseEvents.Released(e));
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        EventDispatcher.dispatch(new MouseEvents.Moved(e));
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
        EventDispatcher.dispatch(new MouseEvents.Dragged(e));
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
        EventDispatcher.dispatch(new MouseEvents.WheelMoved(e));
    }

    @Override
    public void keyTyped(KeyEvent e) {
        EventDispatcher.dispatch(new KeyEvents.Typed(e));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        EventDispatcher.dispatch(new KeyEvents.Pressed(e));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        EventDispatcher.dispatch(new KeyEvents.Released(e));
    }
}
