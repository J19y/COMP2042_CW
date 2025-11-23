package com.comp2042.tetris.ui.view;

import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GameOverPanel extends BorderPane {

    private final Label gameOverLabel;
    private final Label scoreLabel;
    private final Button retryButton;
    private final Button menuButton;
    private Runnable retryAction = () -> {};
    private Runnable returnAction = () -> {};

    public GameOverPanel() {
        gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("game-over-text");
        scoreLabel = new Label("Final Score: 0");
        scoreLabel.getStyleClass().add("game-over-score");

        retryButton = new Button("Retry");
        menuButton = new Button("Main Menu");
        retryButton.getStyleClass().add("menu-button");
        menuButton.getStyleClass().add("menu-button");
        retryButton.setDisable(true);
        menuButton.setDisable(true);

        retryButton.setOnAction(e -> {
            hide();
            retryAction.run();
        });
        menuButton.setOnAction(e -> {
            hide();
            returnAction.run();
        });

        VBox actions = new VBox(10, retryButton, menuButton);
        actions.setAlignment(Pos.CENTER);
        actions.getStyleClass().add("game-over-actions");

        VBox content = new VBox(18, gameOverLabel, scoreLabel, actions);
        content.setAlignment(Pos.CENTER);
        setCenter(content);
        getStyleClass().add("game-over-panel");

        resetLabelState();
    }
    
    public void show(int finalScore) {
        scoreLabel.setText("Final Score: " + finalScore);
        super.setVisible(true);
        retryButton.setDisable(false);
        menuButton.setDisable(false);
        playAnimation();
    }
    
    public void hide() {
        super.setVisible(false);
        resetLabelState();
        retryButton.setDisable(true);
        menuButton.setDisable(true);
    }
    
    private void playAnimation() {
        gameOverLabel.setOpacity(1);
        // Entrance slam + drop
        ScaleTransition slam = new ScaleTransition(Duration.millis(180), gameOverLabel);
        slam.setFromX(5.0);
        slam.setFromY(5.0);
        slam.setToX(1.0);
        slam.setToY(1.0);
        slam.setInterpolator(javafx.animation.Interpolator.EASE_IN);

        TranslateTransition drop = new TranslateTransition(Duration.millis(600), gameOverLabel);
        drop.setFromY(-180);
        drop.setToY(0);
        drop.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        ParallelTransition intro = new ParallelTransition(slam, drop);

        // Glitch shimmer after drop
        Timeline glitch = new Timeline(
            new KeyFrame(Duration.millis(60), e -> {
                gameOverLabel.setTranslateX((Math.random() - 0.5) * 12);
                gameOverLabel.setTranslateY((Math.random() - 0.5) * 6);
                gameOverLabel.setOpacity(0.8 + Math.random() * 0.2);
                if (Math.random() > 0.7) {
                    gameOverLabel.setText("G@ME 0VER");
                } else if (Math.random() > 0.86) {
                    gameOverLabel.setText("GAME_OVER");
                } else {
                    gameOverLabel.setText("GAME OVER");
                }
            })
        );
        glitch.setCycleCount(20);
        glitch.setOnFinished(e -> {
            gameOverLabel.setTranslateX(0);
            gameOverLabel.setTranslateY(0);
            gameOverLabel.setOpacity(1);
            gameOverLabel.setText("GAME OVER");
        });

        new SequentialTransition(intro, glitch).play();
    }

    private void resetLabelState() {
        gameOverLabel.setScaleX(0);
        gameOverLabel.setScaleY(0);
        gameOverLabel.setOpacity(0);
        gameOverLabel.setTranslateY(-180);
        gameOverLabel.setTranslateX(0);
        gameOverLabel.setText("GAME OVER");
        scoreLabel.setText("Final Score: 0");
    }

    public void setOnRetry(Runnable retryAction) {
        this.retryAction = retryAction != null ? retryAction : () -> {};
    }

    public void setOnReturnToMenu(Runnable returnAction) {
        this.returnAction = returnAction != null ? returnAction : () -> {};
    }

}
