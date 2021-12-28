package com.pandaawake.gourdgame.utils;

public enum Direction {

    left("left"),
    up("up"),
    right("right"),
    down("down");

    private String name = "";
    Direction(String name) {
        this.name = name;
    }

    Direction() {}

    public String toString() {
        return name;
    }

}