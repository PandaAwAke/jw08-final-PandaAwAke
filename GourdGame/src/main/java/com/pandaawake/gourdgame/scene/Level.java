package com.pandaawake.gourdgame.scene;

import com.mandas.tiled2d.utils.IntPair;

import java.util.ArrayList;

public class Level {

    private final String[] levelTileMap;
    public ArrayList<IntPair> humanPlayerPositions, computerPlayerPositions;

    public Level(String[] levelTileMap, ArrayList<IntPair> humanPlayerPositions, ArrayList<IntPair> computerPlayerPositions) {
        this.levelTileMap = levelTileMap;
        this.humanPlayerPositions = humanPlayerPositions;
        this.computerPlayerPositions = computerPlayerPositions;
    }

    public String[] getLevelTileMap() {
        return levelTileMap;
    }

}
