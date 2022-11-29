package com.github.mfnsvrtm.snakegame.ui;

import com.github.mfnsvrtm.snakegame.logic.util.Direction;
import com.github.mfnsvrtm.snakegame.model.GameModel;
import com.github.mfnsvrtm.snakegame.model.WorldModel;
import com.github.mfnsvrtm.snakegame.concurrent.ConcurrentGame;
import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;

public class GameComponent extends Canvas {

    private ConcurrentGame game;
    private GameRenderer renderer;

    private final BooleanProperty gameRunning = new SimpleBooleanProperty(false);
    private final IntegerProperty gameScore = new SimpleIntegerProperty(0);

    public GameComponent() {
        super(800, 600);

        this.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.setOnKeyPressed(onKeyPressed);
            }
        });
    }

    public void setWorld(WorldModel world) {
        this.game = new ConcurrentGame(world.width(), world.height());
        this.renderer = new GameRenderer(world, this);
    }

    public void startGame() {
        game.start();
        new AnimationTimer() {

            private GameModel oldModel = null;

            @Override
            public void handle(long l) {
                var model = game.currentState();
                if (model != oldModel) {
                    update(model);
                    render(model);
                    oldModel = model;
                }
            }

        }.start();
    }

    public void restartGame() {
        endGame();
        startGame();
    }

    private void endGame() {
        game.stop();
        // I intentionally don't stop the AnimationTimer. That way food keeps spawning.
    }

    private void update(GameModel model) {
        setGameRunning(model.running());
        setGameScore(model.score());
    }

    private void render(GameModel model) {
        renderer.render(model, this);
    }

    private final EventHandler<KeyEvent> onKeyPressed = (e) -> {
        if (e.getCode().isArrowKey()) {
            this.game.turn(switch (e.getCode()) {
                case DOWN -> Direction.DOWN;
                case UP -> Direction.UP;
                case LEFT -> Direction.LEFT;
                case RIGHT -> Direction.RIGHT;
                default -> null;
            });
        }
    };

    public void setGameRunning(boolean value) {
        gameRunning.set(value);
    }

    public BooleanProperty gameRunningProperty() {
        return gameRunning;
    }

    public void setGameScore(int value) {
        gameScore.set(value);
    }

    public IntegerProperty gameScoreProperty() {
        return gameScore;
    }

}
