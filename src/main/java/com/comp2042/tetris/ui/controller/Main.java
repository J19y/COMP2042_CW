package com.comp2042.tetris.ui.controller;

import com.comp2042.tetris.application.port.GameplayPort;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application entry point for the Tetris game.
 * <p>
 * Initializes the JavaFX application, loads the main menu FXML,
 * and sets up the primary stage.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public class Main extends Application {

    private GameplayPort gameplayPort;
    
    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if FXML loading fails
     */
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

    /**
     * Gets the gameplay port instance.
     *
     * @return the gameplay port
     */
    public GameplayPort getGameplayPort() {
        return gameplayPort;
    }

    /**
     * Application entry point.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

