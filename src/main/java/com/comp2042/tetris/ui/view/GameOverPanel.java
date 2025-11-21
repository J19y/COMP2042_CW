package com.comp2042.tetris.ui.view;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class GameOverPanel extends BorderPane {

    private final Label gameOverLabel;

    public GameOverPanel() {
        gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("glitch-text");
        setCenter(gameOverLabel);
        
        // Initial state
        gameOverLabel.setScaleX(0);
        gameOverLabel.setScaleY(0);
        gameOverLabel.setOpacity(0);
    }
    
    public void show() {
        super.setVisible(true);
        playAnimation();
    }
    
    public void hide() {
        super.setVisible(false);
        gameOverLabel.setScaleX(0);
        gameOverLabel.setScaleY(0);
        gameOverLabel.setOpacity(0);
    }
    
    private void playAnimation() {
        gameOverLabel.setOpacity(1);
        
        // 1. Slam Effect
        ScaleTransition slam = new ScaleTransition(Duration.millis(150), gameOverLabel);
        slam.setFromX(5.0);
        slam.setFromY(5.0);
        slam.setToX(1.0);
        slam.setToY(1.0);
        slam.setInterpolator(javafx.animation.Interpolator.EASE_IN);

        // 2. Glitch Effect
        Timeline glitch = new Timeline(
            new KeyFrame(Duration.millis(60), e -> {
                // Random jitter
                gameOverLabel.setTranslateX((Math.random() - 0.5) * 15);
                gameOverLabel.setTranslateY((Math.random() - 0.5) * 8);
                
                // Random opacity flicker
                gameOverLabel.setOpacity(0.6 + Math.random() * 0.4);
                
                // Text corruption
                if (Math.random() > 0.7) {
                    gameOverLabel.setText("G@ME 0VER");
                } else if (Math.random() > 0.8) {
                    gameOverLabel.setText("GAME_OVER");
                } else {
                    gameOverLabel.setText("GAME OVER");
                }
            })
        );
        glitch.setCycleCount(30); // ~1.8 seconds of glitching
        glitch.setOnFinished(e -> {
            gameOverLabel.setTranslateX(0);
            gameOverLabel.setTranslateY(0);
            gameOverLabel.setOpacity(1);
            gameOverLabel.setText("GAME OVER");
        });

        new SequentialTransition(slam, glitch).play();
    }

}
