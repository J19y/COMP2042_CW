package com.comp2042.tetris.ui.controller;

import com.comp2042.tetris.engine.board.GameView;
import com.comp2042.tetris.application.session.BaseGameController;
import com.comp2042.tetris.application.session.ClassicGameController;
import com.comp2042.tetris.application.session.MysteryGameController;
import com.comp2042.tetris.application.session.TimedGameController;
import com.comp2042.tetris.services.audio.MusicManager;
import com.comp2042.tetris.ui.view.BufferedGameView;
import com.comp2042.tetris.ui.view.GuiController;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class GameSceneLoader {
    private final Button triggerButton;
    private final MenuAnimationController animationController;

    private BaseGameController activeControllerRef;
    private GameView activeViewRef;

    public GameSceneLoader(Button triggerButton, MenuAnimationController animationController) {
        this.triggerButton = triggerButton;
        this.animationController = animationController;
    }

    
    public BaseGameController getActiveGameController() {
        return activeControllerRef;
    }

    
    public GameView getActiveGameView() {
        return activeViewRef;
    }

    
    public void loadGameSceneWithMode(String mode) {
        
        try {
            if ("RUSH".equals(mode)) {
                MusicManager.getInstance().playTrack(MusicManager.Track.RUSH, 900);
            } else if ("MYSTERY".equals(mode)) {
                MusicManager.getInstance().playTrack(MusicManager.Track.MYSTERY, 900);
            } else {
                MusicManager.getInstance().playTrack(MusicManager.Track.CLASSIC, 900);
            }
        } catch (Exception ignored) {
        }

        
        animationController.playGameSceneWarpTransition(() -> loadGameScene(mode));
    }

    
    private void loadGameScene(String selectedGameMode) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/layout/game.fxml"));
            Parent root = fxmlLoader.load();
            GuiController controller = fxmlLoader.getController();

            Scene scene = new Scene(root, 700, 600);
            scene.getStylesheets().add(getClass().getResource("/ui/styles/window.css").toExternalForm());

            Stage stage = (Stage) triggerButton.getScene().getWindow();
            root.setOpacity(0);
            stage.setScene(scene);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            
            GameView decoratedView = new BufferedGameView(controller);

            
            BaseGameController gameController;
            if ("RUSH".equals(selectedGameMode)) {
                gameController = new TimedGameController(decoratedView);
            } else if ("MYSTERY".equals(selectedGameMode)) {
                gameController = new MysteryGameController(decoratedView);
            } else {
                gameController = new ClassicGameController(decoratedView);
            }

            
            activeControllerRef = gameController;
            activeViewRef = decoratedView;

            
            if (activeControllerRef instanceof MysteryGameController) {
                try {
                    ((GameView) decoratedView).bindLevel(
                            ((MysteryGameController) activeControllerRef).getLevelProperty()
                    );
                } catch (Exception ignored) {
                }
            }

        } catch (IOException e) {
            System.err.println("Failed to load game scene: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



