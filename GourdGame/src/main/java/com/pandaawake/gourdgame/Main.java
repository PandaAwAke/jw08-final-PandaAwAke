package com.pandaawake.gourdgame;

import com.mandas.tiled2d.core.Application;
import com.pandaawake.Config;

public class Main {

    public Main() {}

    public static void main(String[] args) {
        GameApp gameApp = GameApp.getGameApp();
        Application application = new Application(gameApp, Config.WindowTitle);

        application.createWindowAndRun();
    }

}
