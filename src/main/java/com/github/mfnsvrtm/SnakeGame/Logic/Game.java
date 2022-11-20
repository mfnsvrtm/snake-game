package com.github.mfnsvrtm.SnakeGame.Logic;

import com.github.mfnsvrtm.SnakeGame.Model.FoodModel;
import com.github.mfnsvrtm.SnakeGame.Model.SnakeModel;
import com.github.mfnsvrtm.SnakeGame.Model.GameModel;
import com.github.mfnsvrtm.SnakeGame.Logic.Util.Direction;
import com.github.mfnsvrtm.SnakeGame.Logic.Util.Vec2D;

import java.util.HashSet;
import java.util.Set;

public class Game {
    private final World world;
    private final Snake snake;
    private final Set<Vec2D> food;

    private boolean running;
    private int score;


    public Game(int width, int height) {
        this.world = new World(width, height);
        this.snake = makeDefaultSnake();
        this.food = makeDefaultFood();

        this.running = true;
        this.score = 0;
    }


    public boolean tick() {
        if (!running) {
            return false;
        }

        running = snake.move(world, food);
        if (running) {
            updateScore();
        }

        return running;
    }

    public void addFood(Vec2D position) {
        food.add(position);
    }


    public Snake snake() {
        return snake;
    }

    public World world() {
        return world;
    }

    public GameModel state() {
        var snake = new SnakeModel(this.snake.body());
        var food = this.food.stream().map(FoodModel::new).toList();
        return new GameModel(snake, food, running, score);
    }


    private void updateScore() {
        score += snake.size();
    }


    private static Snake makeDefaultSnake() {
        return new Snake(new Vec2D(0, 0), Direction.RIGHT);
    }

    private static Set<Vec2D> makeDefaultFood() {
        return new HashSet<>();
    }
}
