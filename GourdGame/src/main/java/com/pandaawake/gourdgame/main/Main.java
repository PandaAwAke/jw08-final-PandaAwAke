package com.pandaawake.gourdgame.main;

import com.mandas.tiled2d.core.Application;
import com.pandaawake.gourdgame.Config;

public class Main {

    public static void main(String[] args) {
        GameApp gameApp = new GameApp();
        Application application = new Application(gameApp, Config.WindowTitle);

        application.createWindowAndRun();
    }

}
