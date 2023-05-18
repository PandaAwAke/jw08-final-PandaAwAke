package com.pandaawake.gourdgame.scene;

import com.mandas.tiled2d.core.Log;
import com.mandas.tiled2d.utils.pair.IntPair;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Level {

    public List<String> levelTileMap;
    public List<IntPair> humanPlayerPositions, computerPlayerPositions;
    public List<String> humanPlayerNames, computerPlayerNames;

    public Level(List<String> levelTileMap, List<IntPair> humanPlayerPositions, List<IntPair> computerPlayerPositions,
                 List<String> humanPlayerNames, List<String> computerPlayerNames) {
        this.levelTileMap = levelTileMap;
        this.humanPlayerPositions = humanPlayerPositions;
        this.computerPlayerPositions = computerPlayerPositions;
        this.humanPlayerNames = humanPlayerNames;
        this.computerPlayerNames = computerPlayerNames;
    }


    private final String separator = "-----";

    public Level(String filename) {
        levelTileMap = new ArrayList<>();
        humanPlayerPositions = new ArrayList<>();
        computerPlayerPositions = new ArrayList<>();
        humanPlayerNames = new ArrayList<>();
        computerPlayerNames = new ArrayList<>();

        BufferedReader reader;
        File levelFile = new File(filename);
        try {
            reader = new BufferedReader(new FileReader(levelFile));
        } catch (FileNotFoundException e) {
            Log.app().error(this.getClass().getName() + ": Level file not found!");
            return;
        }

        String line = "";

        int state = 0;  // 0: tileMap, 1: humanPositions, 2: computerPositions, 3: humanNames, 4: computerNames
        boolean parsing = true;

        while (parsing) {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                Log.app().error(this.getClass().getName() + ": getLine - IOException!");
            }
            if (line == null) {
                break;
            }

            switch (state) {
                case 0:
                    if (!line.equals(separator)) {
                        levelTileMap.add(line);
                    } else {
                        state = 1;
                    }
                    break;
                case 1:
                    if (!line.equals(separator)) {
                        String[] positions = line.split(" ");
                        for (String position : positions) {
                            String[] coords = position.substring(1, position.length() - 1).split(",", 2);
                            int first = Integer.parseInt(coords[0]);
                            int second = Integer.parseInt(coords[1]);
                            humanPlayerPositions.add(new IntPair(first, second));
                        }
                    } else {
                        state = 2;
                    }
                    break;
                case 2:
                    if (!line.equals(separator)) {
                        String[] positions = line.split(" ");
                        for (String position : positions) {
                            String[] coords = position.substring(1, position.length() - 1).split(",", 2);
                            int first = Integer.parseInt(coords[0]);
                            int second = Integer.parseInt(coords[1]);
                            computerPlayerPositions.add(new IntPair(first, second));
                        }
                    } else {
                        state = 3;
                    }
                    break;
                case 3:
                    if (!line.equals(separator)) {
                        String[] names = line.split(" ");
                        humanPlayerNames.addAll(Arrays.asList(names));
                    } else {
                        state = 4;
                    }
                    break;
                case 4:
                    if (!line.equals(separator)) {
                        String[] names = line.split(" ");
                        computerPlayerNames.addAll(Arrays.asList(names));
                    } else {
                        state = 5;
                    }
                    break;
                default:
                    parsing = false;
                    break;
            }
        }
    }
}
