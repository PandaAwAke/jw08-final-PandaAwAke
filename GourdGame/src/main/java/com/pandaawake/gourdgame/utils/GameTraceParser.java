package com.pandaawake.gourdgame.utils;

import com.mandas.tiled2d.core.Log;
import com.mandas.tiled2d.utils.LogParser;
import com.pandaawake.gourdgame.player.action.Actions;
import com.pandaawake.gourdgame.player.action.PlayerAction;

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
        String[] params = lineContent.split(" ", 3);

        assert params.length >= 2;

        // Player name
        String playerName = params[0];

        // Elapsed Time
        Date time = logLine.time;

        // Action kind
        String actionKind = params[1];

        PlayerAction action = null;

        switch (actionKind) {
            case "NoAction":
                action = new Actions.PlayerNoAction();
                break;
            case "DoMove":
                assert params.length == 3;
                String directionStr = params[2];
                Direction direction = null;
                switch (directionStr) {
                    case "left":
                        direction = Direction.left;
                        break;
                    case "up":
                        direction = Direction.up;
                        break;
                    case "right":
                        direction = Direction.right;
                        break;
                    case "down":
                        direction = Direction.down;
                        break;
                }
                action = new Actions.PlayerDoMove(direction);
                break;
            case "ExplodeBomb":
                action = new Actions.PlayerExplodeBomb();
                break;
            case "SetBomb":
                action = new Actions.PlayerSetBomb();
                break;
            default:
                Log.app().error(this.getClass().getName() + ": Illegal action kind!");
                break;
        }

        action.playerName = playerName;
        action.time = time;

        return action;
    }

}
