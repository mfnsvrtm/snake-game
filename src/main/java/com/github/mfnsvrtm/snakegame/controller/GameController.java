package com.github.mfnsvrtm.snakegame.controller;


import com.github.mfnsvrtm.snakegame.model.WorldModel;
import com.github.mfnsvrtm.snakegame.ui.SnakeGameView;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public StackPane root;
    public SnakeGameView gameView;
    public Label score;
    public Label finalScore;
    public VBox scoreContainer;
    public VBox startMenu;
    public VBox gameOverMenu;

    private final BooleanProperty startedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameOverProperty = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameView.setWorld(new WorldModel(20, 20));

        score.textProperty().bind(gameView.gameScoreProperty().asString());
        finalScore.textProperty().bind(gameView.gameScoreProperty().asString());

        scoreContainer.visibleProperty().bind(startedProperty.and(gameView.gameRunningProperty()));
        startMenu.visibleProperty().bind(startedProperty.not());
        gameOverMenu.visibleProperty().bind(startedProperty.and(gameView.gameRunningProperty().not()));
    }

    public void onStartAction() {
        startedProperty.set(true);
        gameView.startGame();
    }

    public void onRestartAction() {
        gameOverProperty.set(false);
        gameView.restartGame();
    }

    public void onExitAction() {
        Platform.exit();
    }
}