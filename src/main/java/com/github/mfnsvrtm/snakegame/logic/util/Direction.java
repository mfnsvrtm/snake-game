package com.github.mfnsvrtm.snakegame.logic.util;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public Direction reversed() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
