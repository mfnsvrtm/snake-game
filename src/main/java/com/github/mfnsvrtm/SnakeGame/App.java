package com.github.mfnsvrtm.SnakeGame;

import com.github.mfnsvrtm.SnakeGame.Logic.Game;
import com.github.mfnsvrtm.SnakeGame.Logic.Util.Direction;
import com.github.mfnsvrtm.SnakeGame.Logic.Util.Vec2D;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class App extends Application {
    private Game game;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(800, 600);
        Scene scene = new Scene(new Group(canvas));
        stage.setScene(scene);
        stage.show();

        scene.setOnKeyPressed(e -> {
            if (e.getCode().isArrowKey()) {
                game.snake().turn(switch (e.getCode()) {
                    case DOWN -> Direction.DOWN;
                    case UP -> Direction.UP;
                    case LEFT -> Direction.LEFT;
                    case RIGHT -> Direction.RIGHT;
                    default -> null;
                });
            }
        });

        game = new Game(20, 20);
        game.addFood(new Vec2D(10, 0));

        long now = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                render(canvas.getGraphicsContext2D(), l - now);
                try {
                    Thread.sleep(66);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.start();
    }

    private void render(GraphicsContext gc, long duration) {
        double xMultiplier = 800.0 / game.world().width();
        double yMultiplier = 600.0 / game.world().height();

        var state = game.state();

        gc.clearRect(0, 0, 800, 600);
        gc.setFill(Color.BLACK);
        for (var pos : state.snake().body()) {
            gc.fillRect(pos.x * xMultiplier, pos.y * yMultiplier, xMultiplier, yMultiplier);
        }
        gc.setFill(Color.RED);
        for (var food : state.food()) {
            var pos = food.position();
            gc.fillRect(pos.x * xMultiplier, pos.y * yMultiplier, xMultiplier, yMultiplier);
        }

        game.tick();

        if (duration % 1000 < 5) {
            game.addFood(new Vec2D(new Random().nextInt(0, 21), new Random().nextInt(0, 21)));
        }
    }
}