module com.github.mfnsvrtm.SnakeGame {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.github.mfnsvrtm.snakegame.controller to javafx.fxml;
    exports com.github.mfnsvrtm.snakegame;
}
