package com.github.mfnsvrtm.snakegame.logic.util;

import lombok.EqualsAndHashCode;

@SuppressWarnings("ClassCanBeRecord")
@EqualsAndHashCode
public class Vec2D {
    public final int x;
    public final int y;

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
}

