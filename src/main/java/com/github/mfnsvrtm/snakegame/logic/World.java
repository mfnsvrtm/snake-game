package com.github.mfnsvrtm.snakegame.logic;

import com.github.mfnsvrtm.snakegame.logic.util.Direction;
import com.github.mfnsvrtm.snakegame.logic.util.Vec2D;
import com.github.mfnsvrtm.snakegame.model.WorldModel;

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


    WorldModel model() {
        return new WorldModel(width, height);
    }

    @SuppressWarnings("SameReturnValue")
    private int top() {
        return 0;
    }

    private int bottom() {
        return height - 1;
    }

    @SuppressWarnings("SameReturnValue")
    private int left() {
        return 0;
    }

    private int right() {
        return width - 1;
    }
}
