package com.pandaawake.gourdgame.main;

import com.mandas.tiled2d.core.Log;
import com.mandas.tiled2d.event.EventDispatcher;
import com.mandas.tiled2d.event.codes.KeyCodes;
import com.mandas.tiled2d.event.events.KeyEvents;
import com.pandaawake.gourdgame.Config;
import com.pandaawake.gourdgame.network.GameClient;
import com.pandaawake.gourdgame.network.data.action.ConnectionAction;
import com.pandaawake.gourdgame.network.data.action.PlayerAction;
import com.pandaawake.gourdgame.player.HumanPlayer;
import com.pandaawake.gourdgame.render.RenderCommand;
import com.pandaawake.gourdgame.scene.GameMap;
import com.pandaawake.gourdgame.scene.Scene;
import com.pandaawake.gourdgame.scene.updater.ClientSceneUpdater;
import com.pandaawake.gourdgame.sprites.Calabash;
import com.pandaawake.gourdgame.sprites.Snake;
import com.pandaawake.gourdgame.sprites.Sprite;
import com.pandaawake.gourdgame.utils.Direction;

public class ClientGameApp extends GameApp {

    protected GameMap gameMap;
    protected Scene scene;
    protected HumanPlayer mainPlayer = null;
    protected String mainPlayerName = "";
    protected int mainPlayerSpriteId = -1;

    protected int clientId = -1;

    protected GameClient gameClient;

    public boolean pause = true;
    public void setPause(boolean pause) {
        this.pause = pause;
    }
    public boolean getPause() {
        return pause;
    }

    public Scene getScene() {
        return scene;
    }

    public HumanPlayer getMainPlayer() {
        return mainPlayer;
    }

    public void setMainPlayer(HumanPlayer mainPlayer) {
        this.mainPlayer = mainPlayer;
    }

    public int getMainPlayerSpriteId() {
        Log.app().error(this.getClass().getName() + ": Main player's sprite is not a calabash?!");
        return mainPlayerSpriteId;
    }

    public String getMainPlayerName() {
        return mainPlayerName;
    }

    public void setMainPlayerName(String mainPlayerName) {
        this.mainPlayerName = mainPlayerName;
    }

    public void setMainPlayerSpriteId(int mainPlayerSpriteId) {
        this.mainPlayerSpriteId = mainPlayerSpriteId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }


    public ClientGameApp() {
        gameMap = new GameMap(Config.MapWidth, Config.MapHeight);
        scene = new Scene(gameMap, new ClientSceneUpdater());
        gameClient = new GameClient(this);

        initializeEventDispatcher();
        initializeClient();
    }

    protected void initializeEventDispatcher() {
        EventDispatcher.register(KeyEvents.Pressed.class, e -> OnPlayerKeyPressed(e));
    }

    protected void initializeClient() {
        new Thread(new GameClient.GameClientRunnable(gameClient)).start();
        gameClient.sendAction(new ConnectionAction.ClientEnter(-1));
    }


    public void resetAll() {
        RenderCommand.clear();
        gameMap.resetAll();
        scene.resetAll();

        mainPlayer = null;
        mainPlayerSpriteId = -1;
        mainPlayerName = "";
    }

    protected void checkGameEnds() {
        if (mainPlayer == null) {
            return;
        }
        if (!scene.getSprites().contains(mainPlayer.sprite)) {
            // Game over
            //setPause(true);
            //JOptionPane.showMessageDialog(null, "游戏结束！你输了！");
        } else {
            boolean NoSnakes = true;
            for (Sprite sprite : scene.getSprites()) {
                if (sprite instanceof Snake) {
                    NoSnakes = false;
                    break;
                }
            }
            if (NoSnakes) {
                // Game over
                //setPause(true);
                //JOptionPane.showMessageDialog(null, "游戏结束！恭喜你赢了！");
            }
        }
    }

    // ---------------------- GameApplication Functions ----------------------
    @Override
    public void InitRenderer() {
        RenderCommand.Init(scene);
    }

    @Override
    public void OnRender() {
        scene.OnRender();

        // Paint something on scoreboard
        RenderCommand.clearScoreboard();

        if (mainPlayer != null) {
            int playerLives = mainPlayer.sprite.getLives();
            RenderCommand.drawScoreboardString(25, 250, "HP: " + String.valueOf(playerLives));
            for (int i = 0; i < playerLives; i++) {
                RenderCommand.drawScoreboardTile(Config.TileSize * i, 150,
                        mainPlayer.sprite.getTileTextureRenderComponent().getPositionsAndTextures().get(0).second);
            }
        }
    }

    @Override
    public void OnUpdate(float timestep) {
        if (pause) {
            return;
        }

        if (mainPlayerSpriteId != -1 && mainPlayer == null) {
            for (Sprite sprite : scene.getSprites()) {
                if (sprite.getId() == mainPlayerSpriteId) {
                    mainPlayer = new HumanPlayer((Calabash) sprite, clientId, mainPlayerName);
                    sprite.getCameraComponent().setRenderingCamera(true);
                }
            }
        }

        checkGameEnds();

        //scene.OnUpdate(timestep);
        scene.setEntities(scene.getSprites());
        scene.getSceneUpdater().OnUpdate(timestep);
    }

    @Override
    public void OnDestroy() {

    }


    public void OnPlayerKeyPressed(KeyEvents.Pressed e) {
        if (pause) {
            return;
        }
        if (mainPlayer == null) {
            return;
        }
        Direction direction = null;
        switch (e.getKeyCode()) {
            case KeyCodes.VK_A:
            case KeyCodes.VK_LEFT:
                direction = Direction.left;
                break;
            case KeyCodes.VK_W:
            case KeyCodes.VK_UP:
                direction = Direction.up;
                break;
            case KeyCodes.VK_D:
            case KeyCodes.VK_RIGHT:
                direction = Direction.right;
                break;
            case KeyCodes.VK_S:
            case KeyCodes.VK_DOWN:
                direction = Direction.down;
                break;
            case KeyCodes.VK_J:
            case KeyCodes.VK_1:
                if (mainPlayer.sprite.canSetBomb()) {
                    gameClient.sendAction(new PlayerAction.SetBomb(-1, mainPlayerSpriteId));
                }
                break;
            case KeyCodes.VK_SPACE:
            case KeyCodes.VK_0:
                gameClient.sendAction(new PlayerAction.ExplodeBomb(-1, mainPlayerSpriteId));
                break;
            
        }

        if (direction != null) {
            gameClient.sendAction(new PlayerAction.DoMove(-1, mainPlayerSpriteId, direction));
        }
    }

}
