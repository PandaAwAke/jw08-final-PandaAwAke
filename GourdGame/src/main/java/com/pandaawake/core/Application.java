package com.pandaawake.core;

import com.pandaawake.Config;

import javax.swing.JFrame;

import com.pandaawake.gourdgame.GameApp;
import com.pandaawake.renderer.Renderer;
import com.pandaawake.renderer.RenderCommand;

import java.awt.event.KeyEvent;

public class Application {

    private static Application app = null;
    public static Application getApplication() {
        if (app == null) {
            app = new Application();
        }
        return app;
    }

    public boolean pause = false;
    public void setPause(boolean pause) {
        this.pause = pause;
    }
    public boolean getPause() {
        return pause;
    }

    private final GameApp gameApp;
    private MainWindow window;

    private Application() {
        gameApp = GameApp.getGameApp();
        RenderCommand.Init();
    }

    public void run() {
        window = new MainWindow(Renderer.getRenderer(), this);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        Thread updateThread = new Thread(new ApplicationUpdator(this));
        updateThread.setDaemon(true);
        updateThread.start();
    }

    /**
     * This function will be called when the window repaints.
     */
    public void OnRender() {
        gameApp.OnRender();
    }

    public void OnKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                setPause(!getPause());
                return;
        }
        if (pause) {
            return;
        }
        gameApp.OnKeyPressed(e);
    }

    public void OnUpdate(float timestep) {
        gameApp.OnUpdate(timestep);
        window.repaint();
    }

    public class ApplicationUpdator implements Runnable {

        private long oldTime = 0;
        private Application app;

        public ApplicationUpdator(Application app) {
            this.app = app;
        }

        @Override
        public void run() {
            while (true) {
                if (oldTime == 0) {
                    oldTime = System.currentTimeMillis();
                }
                else {
                    long time = System.currentTimeMillis();
                    if (pause) {
                        oldTime = time;
                    } else {
                        if (time - oldTime >= (1000.0f / Config.MaxFrameRate)) {
                            app.OnUpdate((time - oldTime) / 1000.0f);
                            oldTime = time;
                        }
                    }
                }
            }
        }

    }
    
}
