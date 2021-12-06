package com.pandaawake.gourdgame.tiles;

import com.mandas.tiled2d.utils.IntPair;

public class Tile {

    private Thing thing;
    private int xPos;
    private int yPos;

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public IntPair getIntPair() {
        return new IntPair(xPos, yPos);
    }

    public Tile() {
        this.thing = null;
        this.xPos = -1;
        this.yPos = -1;
    }

    public Tile(int xPos, int yPos) {
        this.thing = null;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
    }

    public Thing getThing() {
        return thing;
    }

}
