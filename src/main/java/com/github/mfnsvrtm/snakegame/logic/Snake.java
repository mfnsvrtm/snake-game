package com.github.mfnsvrtm.snakegame.logic;

import com.github.mfnsvrtm.snakegame.logic.util.Direction;
import com.github.mfnsvrtm.snakegame.logic.util.Vec2D;
import com.github.mfnsvrtm.snakegame.model.SnakeModel;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Snake {

    private final Game game;
    private final Deque<Vec2D> body;
    private Direction direction;

    Snake(Game game, Vec2D position, Direction direction) {
        this.game = game;
        this.body = new ArrayDeque<>(List.of(position));
        this.direction = direction;
    }

    boolean move() {
        Vec2D newHead = game.world.move(body.getFirst(), direction);

        if (game.food.contains(newHead)) {
            body.addFirst(newHead);
            game.food.remove(newHead);
            return true;
        }

        if (body.contains(newHead)) {
            return false;
        }

        body.addFirst(newHead);
        body.removeLast();
        return true;
    }

    public void turn(Direction direction) {
        if (this.direction.reversed() != direction) {
            this.direction = direction;
        }
    }

    boolean contains(Vec2D position) {
        return body.contains(position);
    }

    int size() {
        return body.size();
    }

    SnakeModel getModel() {
        return new SnakeModel(body);
    }
    
}
