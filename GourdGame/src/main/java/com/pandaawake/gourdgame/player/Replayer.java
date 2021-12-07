package com.pandaawake.gourdgame.player;

import com.mandas.tiled2d.Config;
import com.pandaawake.gourdgame.main.GameApp;
import com.pandaawake.gourdgame.utils.GameTraceParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Replayer {

    private GameTraceParser parser;
    private float elapsedTime = 0.0f;
    private GameTraceParser.PlayerAction playerAction = null;
    private boolean running = true;
    private Set<Player> players;
    private float nextActionDeltaTime = 1.0f;

    public Replayer(Set<Player> players) {
        parser = new GameTraceParser(Config.LogFilename);
        this.players = players;
        getNextAction();
    }

    public void getNextAction() {
        GameTraceParser.PlayerAction nextAction = parser.getAnAction();
        if (nextAction == null) {
            running = false;
            return;
        }

        if (playerAction == null) {
            playerAction = nextAction;
            return;
        }

        long deltaTime = nextAction.time.getTime() - playerAction.time.getTime();
        nextActionDeltaTime = (float) deltaTime / 1000.0f;

        playerAction = nextAction;
    }

    public void performAction() {
        List<Player> matchedPlayers = new ArrayList<>();
        List<Player> matchedPlayers1 = new ArrayList<>();
        for (Player player : players) {
            if (player.getName().equals(playerAction.playerName)) {
                matchedPlayers1.add(player);
            }
        }

        switch (playerAction.playerKind) {
            case GameTraceParser.PlayerAction.HUMAN_PLAYER:
                for (Player player : matchedPlayers1) {
                    if (player instanceof HumanPlayer) {
                        matchedPlayers.add(player);
                    }
                }
                break;
            case GameTraceParser.PlayerAction.COMPUTER_PLAYER:
                for (Player player : matchedPlayers1) {
                    if (player instanceof ComputerPlayer) {
                        matchedPlayers.add(player);
                    }
                }
                break;
        }

        assert !matchedPlayers.isEmpty();
        Player matchedPlayer = matchedPlayers.get(0);

        switch (playerAction.actionKind) {
            case GameTraceParser.PlayerAction.NO_ACTION:
                break;
            case GameTraceParser.PlayerAction.TRY_MOVE_ACTION:
                assert playerAction.moveDirection != null;
                matchedPlayer.doMove(playerAction.moveDirection);
                break;
            case GameTraceParser.PlayerAction.EXPLODE_BOMB_ACTION:
                matchedPlayer.explodeBomb();
                break;
            case GameTraceParser.PlayerAction.SET_BOMB_ACTION:
                matchedPlayer.setBomb();
                break;
        }
    }

    public void OnUpdate(float timestep) {
        if (!running) {
            GameApp.getGameApp().setPause(true);
            return;
        }
        elapsedTime += timestep;
        if (elapsedTime >= nextActionDeltaTime) {
            elapsedTime = 0.0f;
            performAction();
            getNextAction();
        }
    }

}
