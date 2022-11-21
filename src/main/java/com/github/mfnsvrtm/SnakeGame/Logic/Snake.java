package com.github.mfnsvrtm.SnakeGame.Logic;

import com.github.mfnsvrtm.SnakeGame.Logic.Util.Direction;
import com.github.mfnsvrtm.SnakeGame.Logic.Util.Vec2D;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Set;

public class Snake {
    private final Deque<Vec2D> body;
    private Direction direction;


    Snake(Vec2D position, Direction direction) {
        this.body = new ArrayDeque<>(List.of(position));
        this.direction = direction;
    }


    boolean move(World world, Set<Vec2D> food) {
        Vec2D newHead = world.move(body.getFirst(), direction);

        if (food.contains(newHead)) {
            body.addFirst(newHead);
            food.remove(newHead);
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


    Iterable<Vec2D> body() {
        return body;
    }

    int size() {
        return body.size();
    }
}
