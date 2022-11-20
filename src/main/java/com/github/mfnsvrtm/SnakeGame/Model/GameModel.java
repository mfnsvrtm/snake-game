package com.github.mfnsvrtm.SnakeGame.Model;

public record GameModel(SnakeModel snake, Iterable<FoodModel> food, boolean running, int score) { }