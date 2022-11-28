package com.github.mfnsvrtm.snakegame.concurrent.task;

import com.github.mfnsvrtm.snakegame.logic.util.Vec2D;
import com.github.mfnsvrtm.snakegame.model.GameModel;

import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class FoodTask extends TimerTask {
    private final BlockingQueue<Vec2D> foodQueue;
    private final AtomicReference<GameModel> modelAtomic;

    public FoodTask(AtomicReference<GameModel> modelAtomic, BlockingQueue<Vec2D> foodQueue) {
        this.modelAtomic = modelAtomic;
        this.foodQueue = foodQueue;
    }

    @Override
    public void run() {
        var world = modelAtomic.get().world();
        var random = ThreadLocalRandom.current();
        if (random.nextFloat() < 0.02) {
            var x = random.nextInt(0, world.width());
            var y = random.nextInt(0, world.height());
            foodQueue.add(new Vec2D(x, y));
        }
    }
}
