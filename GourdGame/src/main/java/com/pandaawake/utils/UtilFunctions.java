package com.pandaawake.utils;

import com.pandaawake.Config;

public class UtilFunctions {

    public static int PositionInTilesToIndex(int x, int y) {
        return y * Config.TileFileWidth + x;
    }


}
