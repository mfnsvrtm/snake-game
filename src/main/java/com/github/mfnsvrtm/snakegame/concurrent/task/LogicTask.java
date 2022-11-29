package com.github.mfnsvrtm.snakegame.concurrent.task;

import com.github.mfnsvrtm.snakegame.logic.Game;
import com.github.mfnsvrtm.snakegame.logic.util.Direction;
import com.github.mfnsvrtm.snakegame.logic.util.Vec2D;
import com.github.mfnsvrtm.snakegame.model.GameModel;

import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class LogicTask extends TimerTask {

    private final Game game;
    private final AtomicReference<GameModel> modelAtomic;
    private final AtomicReference<Direction> turnDirectionAtomic;
    private final BlockingQueue<Vec2D> foodQueue;

    public LogicTask(Game game, AtomicReference<GameModel> modelAtomic, AtomicReference<Direction> turnDirectionAtomic,
                     BlockingQueue<Vec2D> foodQueue) {
        this.game = game;
        this.modelAtomic = modelAtomic;
        this.turnDirectionAtomic = turnDirectionAtomic;
        this.foodQueue = foodQueue;
    }

    @Override
    public void run() {
        var running = game.tick();
        modelAtomic.set(game.currentState());

        // If turnDirection gets updated after the .get() and before the .getAndSet(), that's fine. If that
        // happens, it should still be non-null. The only atomic part that I care about is .getAndSet().
        if (running && turnDirectionAtomic.get() != null) {
            game.snake().turn(turnDirectionAtomic.getAndSet(null));
        }

        while (true) {
            var food = foodQueue.poll();
            if (food == null) break;
            game.food().add(food);
        }
    }

}
