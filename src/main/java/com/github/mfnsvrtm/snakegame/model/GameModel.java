package com.github.mfnsvrtm.snakegame.model;

public record GameModel(SnakeModel snake, FoodModel food, WorldModel world, boolean running, int score) { }