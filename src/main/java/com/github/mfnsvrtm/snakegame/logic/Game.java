package com.github.mfnsvrtm.snakegame.logic;

import com.github.mfnsvrtm.snakegame.model.GameModel;
import com.github.mfnsvrtm.snakegame.logic.util.Direction;
import com.github.mfnsvrtm.snakegame.logic.util.Vec2D;

public class Game implements Stateful<GameModel> {

    final World world;
    final Snake snake;
    final Food food;

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

        running = snake.move();
        if (running) {
            updateScore();
        }

        return running;
    }

    public Snake snake() {
        return snake;
    }

    public Food food() {
        return food;
    }

    @Override
    public GameModel currentState() {
        var snake = this.snake.currentState();
        var food = this.food.currentState();
        var world = this.world.currentState();
        return new GameModel(snake, food, world, running, score);
    }

    private void updateScore() {
        score += snake.size();
    }

    private Snake makeDefaultSnake() {
        return new Snake(this, new Vec2D(0, 0), Direction.RIGHT);
    }

    private Food makeDefaultFood() {
        return new Food(this);
    }
    
}
