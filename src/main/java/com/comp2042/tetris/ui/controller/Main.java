package com.comp2042.tetris.ui.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.comp2042.tetris.application.port.GameplayPort;

public class Main extends Application {

    private GameplayPort gameplayPort;
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/layout/menu.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 700, 600);
        primaryStage.setTitle("Tetris");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.show();
    }

    public GameplayPort getGameplayPort() {
        return gameplayPort;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

