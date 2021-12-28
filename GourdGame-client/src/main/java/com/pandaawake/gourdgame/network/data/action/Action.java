package com.pandaawake.gourdgame.network.data.action;

public abstract class Action {

    public int senderClientId;

    public Action(int senderClientId) {
        this.senderClientId = senderClientId;
    }

}
