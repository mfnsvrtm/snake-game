package com.github.mfnsvrtm.SnakeGame.Logic;

import com.github.mfnsvrtm.SnakeGame.Logic.Util.Direction;
import com.github.mfnsvrtm.SnakeGame.Logic.Util.Vec2D;

public class World {
    private final int width;
    private final int height;

    World(int width, int height) {
        this.width = width;
        this.height = height;
    }

    Vec2D move(Vec2D position, Direction direction) {
        if (position.y == top() && direction == Direction.UP) {
            return new Vec2D(position.x, bottom());
        } else if (position.y == bottom() && direction == Direction.DOWN) {
            return new Vec2D(position.x, top());
        } else if (position.x == left() && direction == Direction.LEFT) {
            return new Vec2D(right(), position.y);
        } else if (position.x == right() && direction == Direction.RIGHT) {
            return new Vec2D(left(), position.y);
        }

        return position.moved(direction);
    }


    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    private int top() {
        return 0;
    }

    private int bottom() {
        return height - 1;
    }

    private int left() {
        return 0;
    }

    private int right() {
        return width - 1;
    }
}
