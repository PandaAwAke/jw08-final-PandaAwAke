package com.pandaawake.gourdgame.main;

import com.mandas.tiled2d.core.Application;
import com.pandaawake.gourdgame.Config;

public class Main {

    public static void main(String[] args) {
        ControllerApp app = new ControllerApp();
        Application engineApp = new Application(app, Config.WindowTitle);

        engineApp.createWindowAndRun();
    }

}
