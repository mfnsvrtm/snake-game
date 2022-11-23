package com.github.mfnsvrtm.SnakeGame.Controller;

import com.github.mfnsvrtm.SnakeGame.Logic.Game;
import com.github.mfnsvrtm.SnakeGame.Logic.Util.Direction;
import com.github.mfnsvrtm.SnakeGame.Logic.Util.Vec2D;

import com.github.mfnsvrtm.SnakeGame.Model.GameModel;
import com.github.mfnsvrtm.SnakeGame.Model.WorldModel;
import com.github.mfnsvrtm.SnakeGame.Task.FoodTask;
import com.github.mfnsvrtm.SnakeGame.Task.LogicTask;
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
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class GameController implements Initializable {
    public StackPane root;
    public Canvas canvas;
    public Label score;
    public Label finalScore;
    public VBox scoreContainer;
    public VBox startMenu;
    public VBox gameOverMenu;

    private AnimationTimer animationTimer;
    private Timer logicTimer;
    private Timer foodTimer;

    private RenderMetrics metrics;

    private Game game ;
    private AtomicReference<GameModel> modelAtomic;
    private AtomicReference<Direction> turnDirectionAtomic;
    private BlockingQueue<Vec2D> foodQueue;

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

    private void initLogic() {
        game = new Game(20, 30);
        modelAtomic = new AtomicReference<>(game.model());
        turnDirectionAtomic = new AtomicReference<>(null);
        foodQueue = new LinkedBlockingQueue<>();

        metrics = new RenderMetrics(game.model().world(), canvas);

        var logicTask = new LogicTask(game, modelAtomic, turnDirectionAtomic, foodQueue);
        logicTimer = new Timer(true);
        logicTimer.schedule(logicTask, 0, 50);

        var foodTask = new FoodTask(modelAtomic, foodQueue);
        foodTimer = new Timer(true);
        foodTimer.schedule(foodTask, 0, 50);

        animationTimer = new AnimationTimer() {
            private GameModel oldModel = null;

            @Override
            public void handle(long l) {
                var model = modelAtomic.get();
                if (model != oldModel) {
                    updateLogic(model);
                    updateCanvas(model, canvas.getGraphicsContext2D());
                    oldModel = model;
                }
            }
        };
        animationTimer.start();
    }

    private void terminateLogic() {
        logicTimer.cancel();
        foodTimer.cancel();
        animationTimer.stop();
    }

    private void updateLogic(GameModel model) {
        scoreProperty.set(model.score());

        if (!model.running()) {
            gameOverProperty.set(true);
            onGameOver();
        }
    }

    private void updateCanvas(GameModel model, GraphicsContext gc) {
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

    private void onGameOver() {
        terminateLogic();
    }

    private final EventHandler<KeyEvent> onKeyPressed = (e) -> {
        if (e.getCode().isArrowKey()) {
            turnDirectionAtomic.set(switch (e.getCode()) {
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
        initLogic();
    }

    public void onRestartAction() {
        gameOverProperty.set(false);
        terminateLogic();
        initLogic();
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