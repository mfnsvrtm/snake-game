package com.github.mfnsvrtm.SnakeGame.Controller;

import com.github.mfnsvrtm.SnakeGame.Logic.Game;
import com.github.mfnsvrtm.SnakeGame.Logic.Util.Direction;
import com.github.mfnsvrtm.SnakeGame.Logic.Util.Vec2D;

import com.github.mfnsvrtm.SnakeGame.Model.GameModel;
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
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

// TODO: Move anonymous TimerTask implementations into separate classes
public class GameController implements Initializable {
    public StackPane root;
    public Canvas canvas;
    public Label score;
    public Label finalScore;
    public VBox scoreContainer;
    public VBox finalScoreContainer;

    private AnimationTimer timer;

    // TODO: prevent concurrent access in render (double buffering)
    private volatile GameModel model;
    private final Object turnDirectionLock = new Object();
    private Direction turnDirection;
    private final BlockingQueue<Vec2D> foodQueue = new LinkedBlockingQueue<>();

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

        Timer logicTimer = new Timer(true);
        logicTimer.schedule(new TimerTask() {
            private final Game game = new Game(20, 20);

            @Override
            public void run() {
                var running = game.tick();
                model = game.model();

                if (!running) {
                    cancel();
                    return;
                }

                if (turnDirection != null) {
                    synchronized (turnDirectionLock) {
                        game.snake().turn(turnDirection);
                        turnDirection = null;
                    }
                }

                while (true) {
                    var food = foodQueue.poll();
                    if (food == null) break;
                    game.food().add(food);
                }
            }
        }, 0, 50);

        // TODO: replace hardcoded values (world dimensions)
        Timer foodTimer = new Timer(true);
        foodTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                var random = ThreadLocalRandom.current();
                if (random.nextFloat() < 0.05) {
                    foodQueue.add(new Vec2D(
                            random.nextInt(0, 20),
                            random.nextInt(0, 20)));
                }
            }
        }, 0, 50);

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
        if (!model.running()) {
            runningProperty.set(false);
            onGameOver();
        }

        double cellWidth = canvas.getWidth() / model.world().width();
        double cellHeight = canvas.getHeight() / model.world().height();

        scoreProperty.set(model.score());

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
        for (var pos : model.snake().body()) {
            gc.fillRect(pos.x * cellWidth, pos.y * cellHeight, cellWidth, cellHeight);
        }

        gc.setFill(Color.RED);
        for (var food : model.food()) {
            var pos = food.position();
            gc.fillRect(pos.x * cellWidth, pos.y * cellHeight, cellWidth, cellHeight);
        }
    }

    private void onGameOver() {
        timer.stop();
    }

    private final EventHandler<KeyEvent> onKeyPressed = (e) -> {
        if (e.getCode().isArrowKey()) {
            var direction = switch (e.getCode()) {
                case DOWN -> Direction.DOWN;
                case UP -> Direction.UP;
                case LEFT -> Direction.LEFT;
                case RIGHT -> Direction.RIGHT;
                default -> null;
            };

            synchronized (turnDirectionLock) {
                turnDirection = direction;
            }
        }
    };
}
