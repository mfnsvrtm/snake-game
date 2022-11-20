package com.github.mfnsvrtm.SnakeGame.Logic.Model;

public record GameModel(SnakeModel snake, Iterable<FoodModel> food, boolean running, int score) { }