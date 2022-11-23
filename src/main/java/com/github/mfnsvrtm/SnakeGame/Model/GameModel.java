package com.github.mfnsvrtm.SnakeGame.Model;

public record GameModel(SnakeModel snake, FoodModel food, WorldModel world, boolean running, int score) { }