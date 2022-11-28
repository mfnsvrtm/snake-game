package com.github.mfnsvrtm.snakegame.concurrent;

import com.github.mfnsvrtm.snakegame.logic.Game;
import com.github.mfnsvrtm.snakegame.logic.util.Direction;
import com.github.mfnsvrtm.snakegame.logic.util.Vec2D;
import com.github.mfnsvrtm.snakegame.model.GameModel;
import com.github.mfnsvrtm.snakegame.concurrent.task.FoodTask;
import com.github.mfnsvrtm.snakegame.concurrent.task.LogicTask;

import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentGame {

    private final int gameWidth;
    private final int gameHeight;

    private AtomicReference<GameModel> modelAtomic;
    private AtomicReference<Direction> turnDirectionAtomic;

    private Timer logicTimer;
    private Timer foodTimer;

    public ConcurrentGame(int width, int height) {
        this.gameWidth = width;
        this.gameHeight = height;
    }

    public void start() {
        Game game = new Game(gameWidth, gameHeight);
        modelAtomic = new AtomicReference<>(game.getModel());
        turnDirectionAtomic = new AtomicReference<>(null);
        BlockingQueue<Vec2D> foodQueue = new LinkedBlockingQueue<>();

        var logicTask = new LogicTask(game, modelAtomic, turnDirectionAtomic, foodQueue);
        logicTimer = new Timer(true);
        logicTimer.schedule(logicTask, 0, 50);

        var foodTask = new FoodTask(modelAtomic, foodQueue);
        foodTimer = new Timer(true);
        foodTimer.schedule(foodTask, 0, 50);
    }

    public void stop() {
        if (logicTimer != null) logicTimer.cancel();
        if (foodTimer != null) foodTimer.cancel();
    }

    public void turn(Direction turnDirection) {
        if (turnDirectionAtomic != null) {
            turnDirectionAtomic.set(turnDirection);
        } else {
            throw new RuntimeException("Call to turn(Direction) before start() was called.");
        }
    }

    public GameModel model() {
        if (modelAtomic != null) {
            return modelAtomic.get();
        } else {
            throw new RuntimeException("Call to model() before start() was called.");
        }
    }
    
}
