package com.pandaawake.gourdgame.network;

import com.pandaawake.gourdgame.player.Player;
import com.pandaawake.gourdgame.utils.Direction;

import java.util.Collection;

public class Actions {

    // Game Actions
    public static class GameStart extends Action {}
    public static class GamePause extends Action {}
    public static class GameResume extends Action {}
    public static class GameEnd extends Action {}
    public static class GameInitialize extends Action {
        public Collection<Player> players;
        public GameInitialize(Collection<Player> players) { this.players = players; }
    }

    // Player Actions
    public static class PlayerNoAction extends Action {}
    public static class PlayerDoMove extends Action {
        public Direction direction;
        public PlayerDoMove(Direction direction) { this.direction = direction; }
    }
    public static class PlayerSetBomb extends Action {}
    public static class PlayerExplodeBomb extends Action {}

}
