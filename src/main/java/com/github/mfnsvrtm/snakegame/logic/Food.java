package com.github.mfnsvrtm.snakegame.logic;

import com.github.mfnsvrtm.snakegame.logic.util.Vec2D;
import com.github.mfnsvrtm.snakegame.model.FoodModel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Food implements Iterable<Vec2D> {
    private final Game game;
    private final Set<Vec2D> food;


    public Food(Game game) {
        this.game = game;
        this.food = new HashSet<>();
    }


    public void add(Vec2D position) {
        if (!game.snake.contains(position)) {
            food.add(position);
        }
    }

    void remove(Vec2D position) {
        food.remove(position);
    }

    boolean contains(Vec2D position) {
        return food.contains(position);
    }


    FoodModel model() {
        return new FoodModel(food);
    }


    @Override
    public Iterator<Vec2D> iterator() {
        return food.iterator();
    }
}
