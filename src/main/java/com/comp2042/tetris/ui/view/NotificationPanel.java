package com.comp2042.tetris.ui.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * NotificationPanel displays short floating notifications (combat-style text)
 * that pop, float up and fade out. Uses CSS classes `.floating-score` and
 * `.floating-score-high` for styling.
 */
public class NotificationPanel extends StackPane {

    public NotificationPanel() {
        super();
        setMouseTransparent(true); // don't block gameplay clicks
        setPickOnBounds(false);
        setAlignment(Pos.CENTER);
    }

    /**
     * Show a short floating score/message. The label is added to this pane,
     * animated, and removed when complete.
     *
     * @param text the message to display
     */
    public void showScore(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }

        Label label = new Label(text);
        label.setMouseTransparent(true);
        label.getStyleClass().add("floating-score");

        String upper = text.toUpperCase();
        // If text contains high-impact keywords or is long, add high variant
        if (upper.contains("SWEEP") || upper.contains("TETRIS") || upper.length() > 14) {
            label.getStyleClass().add("floating-score-high");
        }

        // Start immediately visible
        label.setScaleX(1.0);
        label.setScaleY(1.0);
        label.setOpacity(1.0);

        getChildren().add(label);

        // Float animation: move up by 50px over 1000ms (keeps text visible longer)
        TranslateTransition floatUp = new TranslateTransition(Duration.millis(1000), label);
        floatUp.setByY(-50);

        // Fade animation: start halfway through float (delay 500ms), last 500ms
        FadeTransition fade = new FadeTransition(Duration.millis(500), label);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.millis(500));

        ParallelTransition combined = new ParallelTransition(floatUp, fade);
        combined.setOnFinished(evt -> getChildren().remove(label));
        combined.play();
    }

    /**
     * Show a score/message at a vertical offset (translateY) within this panel.
     * Positive values move the label down; negative move it up.
     */
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
