package com.github.mfnsvrtm.snakegame.ui;

import com.github.mfnsvrtm.snakegame.model.GameModel;
import com.github.mfnsvrtm.snakegame.model.WorldModel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameRenderer {

    private final RenderMetrics metrics;

    private final Color snakeColor = ColorSchemeConstants.SNAKE_COLOR;
    private final Color foodColor = ColorSchemeConstants.FOOD_COLOR;
    private final Color bgColor = ColorSchemeConstants.BG_COLOR;
    private final Color fgColor = ColorSchemeConstants.FG_COLOR;

    public GameRenderer(WorldModel worldModel, Canvas canvas) {
        this.metrics = new RenderMetrics(worldModel, canvas);
    }

    public void render(GameModel model, Canvas canvas) {
        renderBackground(model.world(), canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        var x = metrics.xOffset;
        var y = metrics.yOffset;
        var size = metrics.cellSize;

        gc.setFill(snakeColor);
        for (var pos : model.snake().body()) {
            gc.fillRect(x + pos.x * size, y + pos.y * size, size, size);
        }

        gc.setFill(foodColor);
        for (var pos : model.food().items()) {
            gc.fillRect(x + pos.x * size, y + pos.y * size, size, size);
        }
    }

    private void renderBackground(WorldModel world, Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(bgColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        var x = metrics.xOffset;
        var y = metrics.yOffset;
        var size = metrics.cellSize;
        
        gc.setFill(fgColor);
        for (int cellY = 0; cellY < world.height(); cellY++) {
            for (int cellX = cellY % 2; cellX < world.width(); cellX += 2) {
                gc.fillRect(x + cellX * size, y + cellY * size, size, size);
            }
        }
    }

    private static class RenderMetrics {

        public final double worldWidth;
        public final double worldHeight;

        public final double cellSize;

        public final double xOffset;
        public final double yOffset;

        public RenderMetrics(WorldModel worldModel, Canvas canvas) {
            double worldRatio = (double) worldModel.width() / (double) worldModel.height();
            double canvasRatio = canvas.getWidth() / canvas.getHeight();

            if (worldRatio < canvasRatio) {
                worldHeight = canvas.getHeight();
                cellSize = worldHeight / worldModel.height();
                worldWidth = cellSize * worldModel.width();
                xOffset = (canvas.getWidth() - worldWidth) / 2;
                yOffset = 0;
            } else {
                worldWidth = canvas.getWidth();
                cellSize = worldWidth / worldModel.width();
                worldHeight = cellSize * worldModel.height();
                yOffset = (canvas.getHeight() - worldHeight) / 2;
                xOffset = 0;
            }
        }
        
    }

}
