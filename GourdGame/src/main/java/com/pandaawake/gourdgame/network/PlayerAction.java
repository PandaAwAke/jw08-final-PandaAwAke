package com.pandaawake.gourdgame.network;

public abstract class PlayerAction extends Action {

    public int playerId = 0;
    public PlayerAction(int playerId) {
        this.playerId = playerId;
    }
    
}
