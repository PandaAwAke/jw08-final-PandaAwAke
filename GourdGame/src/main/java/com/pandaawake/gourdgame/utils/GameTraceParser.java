package com.pandaawake.gourdgame.utils;

import com.mandas.tiled2d.utils.LogParser;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;

import java.util.Date;

public class GameTraceParser {

    private LogParser parser;
    public GameTraceParser(String logFilename) {
        parser = new LogParser(logFilename);
    }

    public PlayerAction getAnAction() {
        LogParser.LogLine logLine = parser.getLine();
        if (logLine == null) {
            return null;
        }
        String lineContent = logLine.content;

        // Elapsed Time
        Date time = logLine.time;

        return PlayerAction.parseString(lineContent, time);
    }

}
