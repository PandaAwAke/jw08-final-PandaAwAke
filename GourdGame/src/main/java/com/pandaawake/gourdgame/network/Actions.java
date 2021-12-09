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
    public static class PlayerNoAction extends PlayerAction {
        public PlayerNoAction(int playerId) {
            super(playerId);
        }
    }
    public static class PlayerDoMove extends PlayerAction {
        public Direction direction;
        public PlayerDoMove(int playerId, Direction direction) {
            super(playerId);
            this.direction = direction;
        }
    }
    public static class PlayerSetBomb extends PlayerAction {
        public PlayerSetBomb(int playerId) {
            super(playerId);
        }
    }
    public static class PlayerExplodeBomb extends PlayerAction {
        public PlayerExplodeBomb(int playerId) {
            super(playerId);
        }
    }

}
