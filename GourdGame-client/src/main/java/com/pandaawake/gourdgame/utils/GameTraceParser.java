package com.pandaawake.gourdgame.utils;

import com.mandas.tiled2d.utils.LogParser;
import com.pandaawake.gourdgame.network.data.action.Action;
import com.pandaawake.gourdgame.network.data.action.GameAction;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;

import java.io.IOException;
import java.util.Date;

public class GameTraceParser {

    private LogParser parser;

    private boolean gameStarted = false;

    public GameTraceParser(String logFilename) {
        parser = new LogParser(logFilename);
    }

    public Action getAnAction() {
        LogParser.LogLine logLine = parser.getLine();
        if (logLine == null) {
            return null;
        }
        String lineContent = logLine.content;
        if (lineContent.equals("Game Started!")) {
            gameStarted = true;
            return new GameAction.GameStart(-1);
        }

        if (gameStarted) {
            // Elapsed Time
            Date time = logLine.time;
            return PlayerAction.parseString(lineContent, time);
        } else {
            String[] clientIdSpriteIdAndName = logLine.content.split(" ", 3);
            return new GameAction.GameInitialize(Integer.parseInt(clientIdSpriteIdAndName[0]),
                    Integer.parseInt(clientIdSpriteIdAndName[1]), clientIdSpriteIdAndName[2]);
        }

    }

    public void Close() throws IOException {
        parser.Close();
    }

}
