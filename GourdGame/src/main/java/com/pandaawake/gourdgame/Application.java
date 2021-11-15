package com.pandaawake.gourdgame;

import com.pandaawake.Config;

import javax.swing.JFrame;

import com.pandaawake.renderer.AsciiFontTile;
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

    private final GameApp gameApp;

    private final Renderer terminal;
    private final RenderCommand renderer;
    private MainWindow window;


    private Application() {
        gameApp = GameApp.getGameApp();
        terminal = new Renderer(Config.MapWidth, Config.MapHeight, AsciiFontTile.GAME_32_32, gameApp.getScene());
        renderer = new RenderCommand(terminal);
    }

    public void run() {
        gameApp.setRenderer(renderer);

        window = new MainWindow(terminal, this);
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
        gameApp.OnKeyPressed(e);
    }

    public void OnUpdate(float timestep) {
        window.repaint();
        gameApp.OnUpdate(timestep);
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
                    if (time - oldTime >= (1000.0f / Config.FrameRate)) {
                        app.OnUpdate((time - oldTime) / 1000.0f);
                        oldTime = time;
                    }
                    
                }
            }
        }

    }
    
}
