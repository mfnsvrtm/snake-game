package com.github.mfnsvrtm.SnakeGame.Model;

public record GameModel(SnakeModel snake, Iterable<FoodModel> food, WorldModel world, boolean running, int score) { }