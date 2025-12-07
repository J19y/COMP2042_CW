package com.comp2042.tetris.ui.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Panel for displaying transient score notifications.
 * Shows animated score bonuses that fade out automatically.
 */
public class NotificationPanel extends StackPane {

    public NotificationPanel() {
        super();
        setMouseTransparent(true); 
        setPickOnBounds(false);
        setAlignment(Pos.CENTER);
    }

    
    public void showScore(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }

        Label label = new Label(text);
        label.setMouseTransparent(true);
        label.getStyleClass().add("floating-score");

        String upper = text.toUpperCase();
        
        if (upper.contains("SWEEP") || upper.contains("TETRIS") || upper.length() > 14) {
            label.getStyleClass().add("floating-score-high");
        }

        
        label.setScaleX(1.0);
        label.setScaleY(1.0);
        label.setOpacity(1.0);

        getChildren().add(label);

        
        TranslateTransition floatUp = new TranslateTransition(Duration.millis(1000), label);
        floatUp.setByY(-50);

        
        FadeTransition fade = new FadeTransition(Duration.millis(500), label);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.millis(500));

        ParallelTransition combined = new ParallelTransition(floatUp, fade);
        combined.setOnFinished(evt -> getChildren().remove(label));
        combined.play();
    }

    
    public void showScore(String text, double offsetY) {
        if (text == null || text.isEmpty()) {
            return;
        }

        Label label = new Label(text);
        label.setMouseTransparent(true);
        label.getStyleClass().add("floating-score");

        String upper = text.toUpperCase();
        if (upper.contains("SWEEP") || upper.contains("TETRIS") || upper.length() > 14) {
            label.getStyleClass().add("floating-score-high");
        }

        label.setScaleX(1.0);
        label.setScaleY(1.0);
        label.setOpacity(1.0);
        label.setTranslateY(offsetY);

        getChildren().add(label);

        TranslateTransition floatUp = new TranslateTransition(Duration.millis(1000), label);
        floatUp.setByY(-50);

        FadeTransition fade = new FadeTransition(Duration.millis(500), label);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.millis(500));

        ParallelTransition combined = new ParallelTransition(floatUp, fade);
        combined.setOnFinished(evt -> getChildren().remove(label));
        combined.play();
    }

}

