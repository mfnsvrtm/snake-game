module com.github.mfnsvrtm.SnakeGame {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    opens com.github.mfnsvrtm.snakegame.controller to javafx.fxml;
    exports com.github.mfnsvrtm.snakegame;
}
