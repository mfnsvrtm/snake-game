package com.github.mfnsvrtm.snakegame.ui;

import com.github.mfnsvrtm.snakegame.logic.util.Direction;
import com.github.mfnsvrtm.snakegame.model.GameModel;
import com.github.mfnsvrtm.snakegame.model.WorldModel;
import com.github.mfnsvrtm.snakegame.threading.ThreadedGame;
import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SnakeGameView extends StackPane {
    private final Canvas canvas;
    private final VBox vBox;
    private final HBox hBox;
    private final Label scoreLabel;
    private final Label scoreValue;

    private ThreadedGame game;

    private GameRenderer renderer;

    private final BooleanProperty scoreVisibility = new SimpleBooleanProperty(true);
    public void setScoreVisibility(boolean value) { scoreVisibility.set(value); }
    public boolean getScoreVisibility() { return scoreVisibility.get(); }

    public BooleanProperty scoreVisibilityProperty() { return scoreVisibility; }
    private final BooleanProperty gameRunning = new SimpleBooleanProperty(false);
    public void setGameRunning(boolean value) { gameRunning.set(value); }
    public boolean getGameRunning() { return gameRunning.get(); }
    public BooleanProperty gameRunningProperty() { return gameRunning; }

    private final IntegerProperty gameScore = new SimpleIntegerProperty(0);
    public void setGameScore(int value) { gameScore.set(value); }
    public int getGameScore() { return gameScore.get(); }
    public IntegerProperty gameScoreProperty() { return gameScore; }

    public SnakeGameView() {
        this.canvas = new Canvas(800, 600);

        this.scoreLabel = new Label("Score: ");
        this.scoreValue = new Label();

        this.hBox = new HBox(scoreLabel, scoreValue);
        this.vBox = new VBox(hBox);

        this.hBox.setAlignment(Pos.CENTER);
        this.vBox.setAlignment(Pos.TOP_CENTER);
        this.vBox.setFillWidth(false);

        this.hBox.getStyleClass().add("small-container");

        this.getChildren().addAll(canvas, vBox);

        this.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.setOnKeyPressed(onKeyPressed);
            }
        });
    }

    public void setWorld(WorldModel world) {
        this.game = new ThreadedGame(world.width(), world.height());
        this.renderer = new GameRenderer(world, canvas);
    }

    public void startGame() {
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

    public void restartGame() {
        endGame();
        startGame();
    }

    private void endGame() {
        game.stop();
        // I intentionally don't stop the AnimationTimer. That way food keeps spawning.
    }

    private void update(GameModel model) {
        scoreValue.setText(String.valueOf(model.score()));
        setGameRunning(model.running());
        setGameScore(model.score());
    }

    private void render(GameModel model) {
        renderer.render(model, canvas);
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
}
