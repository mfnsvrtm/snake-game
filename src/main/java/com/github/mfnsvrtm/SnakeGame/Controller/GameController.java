package com.github.mfnsvrtm.SnakeGame.Controller;

import com.github.mfnsvrtm.SnakeGame.Logic.Game;
import com.github.mfnsvrtm.SnakeGame.Logic.Util.Direction;
import com.github.mfnsvrtm.SnakeGame.Logic.Util.Vec2D;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public StackPane root;
    public Canvas canvas;
    public Label score;
    public Label finalScore;
    public VBox scoreContainer;
    public VBox finalScoreContainer;

    private final Game game = new Game(20, 20);
    private final Random random = new Random();

    private AnimationTimer timer;

    private final IntegerProperty scoreProperty = new SimpleIntegerProperty(0);
    private final BooleanProperty runningProperty = new SimpleBooleanProperty(true);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        score.textProperty().bind(scoreProperty.asString());
        finalScore.textProperty().bind(scoreProperty.asString());

        scoreContainer.visibleProperty().bind(runningProperty);
        finalScoreContainer.visibleProperty().bind(runningProperty.not());

        root.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.setOnKeyPressed(onKeyPressed);
            }
        });

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                render(canvas.getGraphicsContext2D());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.start();
    }

    private void render(GraphicsContext gc) {
        double cellWidth = canvas.getWidth() / game.world().width();
        double cellHeight = canvas.getHeight() / game.world().height();

        var state = game.state();
        scoreProperty.set(state.score());

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
        for (var pos : state.snake().body()) {
            gc.fillRect(pos.x * cellWidth, pos.y * cellHeight, cellWidth, cellHeight);
        }

        gc.setFill(Color.RED);
        for (var food : state.food()) {
            var pos = food.position();
            gc.fillRect(pos.x * cellWidth, pos.y * cellHeight, cellWidth, cellHeight);
        }

        if (!game.tick()) {
            runningProperty.set(false);
            onGameOver();
        }

        if (random.nextFloat() < 0.5) {
            game.addFood(new Vec2D(
                    random.nextInt(0, game.world().width()),
                    random.nextInt(0, game.world().height())));
        }
    }

    private void onGameOver() {
        timer.stop();
    }

    private final EventHandler<KeyEvent> onKeyPressed = (e) -> {
        if (e.getCode().isArrowKey()) {
            game.snake().turn(switch (e.getCode()) {
                case DOWN -> Direction.DOWN;
                case UP -> Direction.UP;
                case LEFT -> Direction.LEFT;
                case RIGHT -> Direction.RIGHT;
                default -> null;
            });
        }
    };
}
