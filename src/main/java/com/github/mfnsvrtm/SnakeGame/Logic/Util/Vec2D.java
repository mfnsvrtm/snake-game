package com.github.mfnsvrtm.SnakeGame.Logic.Util;

import java.util.Objects;

public class Vec2D {
    public int x;
    public int y;

    public Vec2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2D moved(Direction direction) {
        return switch (direction) {
            case UP -> new Vec2D(x, y - 1);
            case DOWN -> new Vec2D(x, y + 1);
            case LEFT -> new Vec2D(x - 1, y);
            case RIGHT -> new Vec2D(x + 1, y);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec2D vec2D = (Vec2D) o;
        return x == vec2D.x && y == vec2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

