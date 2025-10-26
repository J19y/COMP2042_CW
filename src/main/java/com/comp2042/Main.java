package com.comp2042;

import com.comp2042.game.GameController;
import com.comp2042.ui.GuiController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private GameController gameController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gameLayout.fxml"));
        Parent root = fxmlLoader.load();
        GuiController controller = fxmlLoader.getController();

        Scene scene = new Scene(root, 300, 510);
        scene.getStylesheets().add(getClass().getResource("/window_style.css").toExternalForm());
        primaryStage.setTitle("Tetris");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.show();

        gameController = new GameController(controller);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
