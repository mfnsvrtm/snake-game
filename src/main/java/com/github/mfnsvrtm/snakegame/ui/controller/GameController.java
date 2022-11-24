package com.github.mfnsvrtm.snakegame.ui.controller;

import com.github.mfnsvrtm.snakegame.logic.util.Direction;

import com.github.mfnsvrtm.snakegame.model.GameModel;
import com.github.mfnsvrtm.snakegame.model.WorldModel;
import com.github.mfnsvrtm.snakegame.threading.ThreadedGame;
import com.github.mfnsvrtm.snakegame.ui.GameRenderer;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public StackPane root;
    public Canvas canvas;
    public Label score;
    public Label finalScore;
    public VBox scoreContainer;
    public VBox startMenu;
    public VBox gameOverMenu;

    private GameRenderer renderer;

    private final WorldModel world = new WorldModel(20, 20);
    private final ThreadedGame game = new ThreadedGame(world.width(), world.height());

    private final BooleanProperty startedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameOverProperty = new SimpleBooleanProperty(false);
    private final IntegerProperty scoreProperty = new SimpleIntegerProperty(0);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        renderer = new GameRenderer(world, canvas);

        score.textProperty().bind(scoreProperty.asString());
        finalScore.textProperty().bind(scoreProperty.asString());

        scoreContainer.visibleProperty().bind(startedProperty.and(gameOverProperty.not()));
        startMenu.visibleProperty().bind(startedProperty.not());
        gameOverMenu.visibleProperty().bind(gameOverProperty);

        root.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.setOnKeyPressed(onKeyPressed);
            }
        });
    }


    private void startGame() {
        game.start();
        new AnimationTimer() {
            private GameModel oldModel = null;

            @Override
            public void handle(long l) {
                var model = game.model();
                if (model != oldModel) {
                    update(model);
                    render(model);
                    oldModel = model;
                }
            }
        }.start();
    }

    private void endGame() {
        game.stop();
        // I intentionally don't stop the AnimationTimer. That way food keeps spawning.
    }


    private void update(GameModel model) {
        scoreProperty.set(model.score());
        gameOverProperty.set(!model.running());
    }

    private void render(GameModel model) {
        renderer.render(model, canvas);
    }


    private final EventHandler<KeyEvent> onKeyPressed = (e) -> {
        if (e.getCode().isArrowKey()) {
            game.turn(switch (e.getCode()) {
                case DOWN -> Direction.DOWN;
                case UP -> Direction.UP;
                case LEFT -> Direction.LEFT;
                case RIGHT -> Direction.RIGHT;
                default -> null;
            });
        }
    };


    public void onStartAction() {
        startedProperty.set(true);
        startGame();
    }

    public void onRestartAction() {
        gameOverProperty.set(false);
        endGame();
        startGame();
    }

    public void onExitAction() {
        Platform.exit();
    }
}