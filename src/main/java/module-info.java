module com.github.mfnsvrtm.SnakeGame {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.github.mfnsvrtm.SnakeGame to javafx.fxml;
    exports com.github.mfnsvrtm.SnakeGame;
}
