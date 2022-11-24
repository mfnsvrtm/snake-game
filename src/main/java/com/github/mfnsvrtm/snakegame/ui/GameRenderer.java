package com.github.mfnsvrtm.snakegame.ui;

import com.github.mfnsvrtm.snakegame.model.GameModel;
import com.github.mfnsvrtm.snakegame.model.WorldModel;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class GameRenderer {
    private final RenderMetrics metrics;

    private final Color snakeColor = Color.web("#3ecc3e");
    private final Color foodColor = Color.web("#d02d2d");
    private final Color bgColor = Color.web("#582828");
    private final Color fgColor = Color.web("#491e1e");

    public GameRenderer(WorldModel worldModel, Canvas canvas) {
        metrics = new RenderMetrics(worldModel, canvas);
    }

    public void render(GameModel model, Canvas canvas) {
        renderBackground(model.world(), canvas);

        var gc = canvas.getGraphicsContext2D();

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
        var gc = canvas.getGraphicsContext2D();

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
            if (worldModel.width() > worldModel.height()) {
                worldWidth = canvas.getWidth();
                cellSize = worldWidth / worldModel.width();
                worldHeight = cellSize * worldModel.height();
                yOffset = (canvas.getHeight() - worldHeight) / 2;
                xOffset = 0;
            } else {
                worldHeight = canvas.getHeight();
                cellSize = worldHeight / worldModel.height();
                worldWidth = cellSize * worldModel.width();
                xOffset = (canvas.getWidth() - worldWidth) / 2;
                yOffset = 0;
            }
        }
    }
}
