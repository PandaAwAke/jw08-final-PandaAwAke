package com.pandaawake.utils;

import com.pandaawake.Config;

public class UtilFunctions {

    public static char PositionInTilesToChar(int x, int y) {
        return (char)(y * Config.TileFileSize + x);
    }


}
