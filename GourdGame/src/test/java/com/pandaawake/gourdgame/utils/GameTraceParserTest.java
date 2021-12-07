package com.pandaawake.gourdgame.utils;

import com.mandas.tiled2d.Config;
import org.junit.Test;

public class GameTraceParserTest {

    @Test
    public void testGameTraceParser() {
        GameTraceParser parser = new GameTraceParser(Config.LogFilename);
        GameTraceParser.PlayerAction playerAction = null;
        while ((playerAction = parser.getAnAction()) != null) {
            System.out.println(playerAction.toString());
        }

    }

}
