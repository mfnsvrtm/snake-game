package com.github.mfnsvrtm.SnakeGame.Controller;

import com.github.mfnsvrtm.SnakeGame.Logic.Util.Direction;

import com.github.mfnsvrtm.SnakeGame.Model.GameModel;
import com.github.mfnsvrtm.SnakeGame.Model.WorldModel;
import com.github.mfnsvrtm.SnakeGame.Threading.ThreadedGame;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
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
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public StackPane root;
    public Canvas canvas;
    public Label score;
    public Label finalScore;
    public VBox scoreContainer;
    public VBox startMenu;
    public VBox gameOverMenu;

    private AnimationTimer animationTimer;

    private RenderMetrics metrics;
    private final ThreadedGame game = new ThreadedGame(20, 30);

    private final BooleanProperty startedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameOverProperty = new SimpleBooleanProperty(false);
    private final IntegerProperty scoreProperty = new SimpleIntegerProperty(0);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        metrics = new RenderMetrics(game.model().world(), canvas);

        animationTimer = new AnimationTimer() {
            private GameModel oldModel = null;

            @Override
            public void handle(long l) {
                var model = game.model();
                if (model != oldModel) {
                    update(model);
                    render(model, canvas.getGraphicsContext2D());
                    oldModel = model;
                }
            }
        };
        animationTimer.start();
    }

    private void endGame() {
        game.stop();
        // This is intended. I like that food continues to spawn.
        // animationTimer.stop();
    }


    private void update(GameModel model) {
        scoreProperty.set(model.score());
        gameOverProperty.set(!model.running());
    }

    private void render(GameModel model, GraphicsContext gc) {
        var x = metrics.xOffset;
        var y = metrics.yOffset;
        var size = metrics.cellSize;

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
        for (var pos : model.snake().body()) {
            gc.fillRect(x + pos.x * size, y + pos.y * size, size, size);
        }

        gc.setFill(Color.RED);
        for (var food : model.food()) {
            var pos = food.position();
            gc.fillRect(x + pos.x * size, y + pos.y * size, size, size);
        }
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

class RenderMetrics {
    public final double worldWidth;
    public final double worldHeight;

    public final double cellSize;

    public final double xOffset;
    public final double yOffset;

    public RenderMetrics(WorldModel worldModel, Canvas canvas) {
        if (worldModel.width() > worldModel.height()) {
            worldWidth = canvas.getWidth();
            cellSize = worldWidth / worldModel.width();
            worldHeight = cellSize * worldModel.height();
            yOffset = (canvas.getHeight() - worldHeight) / 2;
            xOffset = 0;
        } else {
            worldHeight = canvas.getHeight();
            cellSize = worldHeight / worldModel.height();
            worldWidth = cellSize * worldModel.width();
            xOffset = (canvas.getWidth() - worldWidth) / 2;
            yOffset = 0;
        }
    }
}