package com.pandaawake.gourdgame.scene;

import com.pandaawake.gourdgame.Config;
import org.junit.Test;

public class LevelTest {

    @Test
    public void testLevelParser() {
        Level level = Config.level;
        System.out.println(level.levelTileMap.size());
        System.out.println(level.humanPlayerPositions.size());
        System.out.println(level.computerPlayerPositions.size());
        System.out.println(level.humanPlayerNames.size());
        System.out.println(level.computerPlayerNames.size());
    }

}
