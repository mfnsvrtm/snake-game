package com.github.mfnsvrtm.snakegame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public enum SceneResources {

    Game("game");

    private final static String pathFormatString = "/fxml/%s.fxml";
    private final String sceneName;

    SceneResources(String sceneName) {
        this.sceneName = sceneName;
    }

    public Parent load() throws IOException {
        return new FXMLLoader(App.class.getResource(pathFormatString.formatted(sceneName))).load();
    }

}
