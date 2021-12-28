package com.pandaawake.gourdgame.utils;

import com.mandas.tiled2d.Config;
import com.pandaawake.gourdgame.network.data.action.Action;
import org.junit.Test;

public class GameTraceParserTest {

    @Test
    public void testGameTraceParser() {
        GameTraceParser parser = new GameTraceParser(Config.LogFilename);
        Action action = null;
        while ((action = parser.getAnAction()) != null) {
            System.out.println(action.toString());
        }

    }

}
