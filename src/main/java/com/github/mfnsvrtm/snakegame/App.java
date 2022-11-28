package com.github.mfnsvrtm.snakegame;

import com.github.mfnsvrtm.snakegame.ui.ColorSchemeConstants;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(SceneResources.Game.load(), 800, 600);
        scene.setFill(ColorSchemeConstants.BG_COLOR);
        stage.setTitle("SnakeGame");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    
}