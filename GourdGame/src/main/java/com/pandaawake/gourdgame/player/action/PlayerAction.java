package com.pandaawake.gourdgame.player.action;

import java.util.Date;

import com.mandas.tiled2d.Config;

public abstract class PlayerAction extends Action {
    public int playerId = 0;
    public String playerName = "";
    public Date time = null;

    public PlayerAction() {
        
    }

    public static String toString(PlayerAction action) {
        String result = "";
        result += Config.DateFormat.format(action.time) + " ";
        result += action.playerName + " ";
        if (action instanceof Actions.PlayerNoAction) {
            result += "NoAction ";
        } else if (action instanceof Actions.PlayerDoMove) {
            assert ((Actions.PlayerDoMove) action).direction != null;
            result += "DoMove " + ((Actions.PlayerDoMove) action).direction.toString();
        } else if (action instanceof Actions.PlayerSetBomb) {
            result += "ExplodeBomb ";
        } else if (action instanceof Actions.PlayerExplodeBomb) {
            result += "SetBomb ";
        }
        return result;
    }

    public static PlayerAction parseString(String str) {
        return null;
    }
    
}
