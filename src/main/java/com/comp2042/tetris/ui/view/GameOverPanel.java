package com.comp2042.tetris.ui.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;


public class GameOverPanel extends BorderPane {

    private final Label gameOverLabel;

    public GameOverPanel() {
        gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        setCenter(gameOverLabel);
        
        // Initial state
        gameOverLabel.setScaleX(0);
        gameOverLabel.setScaleY(0);
    }
    
    public void show() {
        super.setVisible(true);
        gameOverLabel.setOpacity(0);
        playAnimation();
    }
    
    public void hide() {
        super.setVisible(false);
        gameOverLabel.setScaleX(0);
        gameOverLabel.setScaleY(0);
        gameOverLabel.setOpacity(0);
    }
    
    private void playAnimation() {
        ScaleTransition st = new ScaleTransition(Duration.millis(500), gameOverLabel);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1);
        st.setToY(1);
        st.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(Duration.millis(500), gameOverLabel);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);

        new ParallelTransition(st, ft).play();
    }

}
