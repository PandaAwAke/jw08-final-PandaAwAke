package com.pandaawake.gourdgame.utils;

import com.mandas.tiled2d.Config;
import com.mandas.tiled2d.utils.LogParser;

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
        PlayerAction result = new PlayerAction();
        String lineContent = logLine.content;
        String[] params = lineContent.split(" ", 5);

        assert params.length >= 3;

        // Player kind
        String playerKind = params[0];
        switch (playerKind) {
            case "Human":
                result.playerKind = PlayerAction.HUMAN_PLAYER;
                break;
            case "Computer":
                result.playerKind = PlayerAction.COMPUTER_PLAYER;
                break;
        }

        // Player name
        result.playerName = params[1];

        // Elapsed Time
        result.time = logLine.time;

        // Action kind
        String actionKind = params[2];
        switch (actionKind) {
            case "NoAction":
                result.actionKind = PlayerAction.NO_ACTION;
                break;
            case "DoMove":
                result.actionKind = PlayerAction.TRY_MOVE_ACTION;
                assert params.length == 4;
                String direction = params[3];
                switch (direction) {
                    case "left":
                        result.moveDirection = Direction.left;
                        break;
                    case "up":
                        result.moveDirection = Direction.up;
                        break;
                    case "right":
                        result.moveDirection = Direction.right;
                        break;
                    case "down":
                        result.moveDirection = Direction.down;
                        break;
                }
                break;
            case "ExplodeBomb":
                result.actionKind = PlayerAction.EXPLODE_BOMB_ACTION;
                break;
            case "SetBomb":
                result.actionKind = PlayerAction.SET_BOMB_ACTION;
                break;
        }

        return result;
    }


    public static class PlayerAction {
        // Player kinds
        public static final int INVALID_PLAYER = 0;
        public static final int HUMAN_PLAYER = 1;
        public static final int COMPUTER_PLAYER = 2;

        // Action kinds
        public static final int NO_ACTION = 0;
        public static final int TRY_MOVE_ACTION = 1;
        public static final int EXPLODE_BOMB_ACTION = 2;
        public static final int SET_BOMB_ACTION = 3;

        public int playerKind = 0;
        public String playerName = "";
        public Date time = null;
        public int actionKind = 0;
        public Direction moveDirection = null;

        public String toString() {
            String result = "";
            result += Config.DateFormat.format(time) + " ";
            switch (playerKind) {
                case INVALID_PLAYER:
                    result += "Invalid ";
                    break;
                case HUMAN_PLAYER:
                    result += "Human ";
                    break;
                case COMPUTER_PLAYER:
                    result += "Computer ";
                    break;
            }
            result += playerName + " ";
            switch (actionKind) {
                case NO_ACTION:
                    result += "NoAction ";
                    break;
                case TRY_MOVE_ACTION:
                    assert moveDirection != null;
                    result += "DoMove " + moveDirection.toString();
                    break;
                case EXPLODE_BOMB_ACTION:
                    result += "ExplodeBomb ";
                    break;
                case SET_BOMB_ACTION:
                    result += "SetBomb ";
                    break;
            }
            return result;
        }
    }


}
