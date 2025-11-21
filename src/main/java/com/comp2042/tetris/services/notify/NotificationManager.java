package com.comp2042.tetris.services.notify;

import com.comp2042.tetris.ui.view.NotificationPanel;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Service responsible for displaying notification messages in the game.
 * Extracted from GuiController to follow Single Responsibility Principle.
 */
public final class NotificationManager {
    
    private final Group notificationContainer;

    /**
     * Creates a new NotificationService.
     * 
     * @param notificationContainer the Group that will contain notification panels
     */
    public NotificationManager(Group notificationContainer) {
        this.notificationContainer = notificationContainer;
    }

    /**
     * Display a score bonus notification.
     * 
     * @param bonus the bonus score to display
     */
    public void showScoreBonus(int bonus) {
        if (notificationContainer == null) {
            return;
        }
        
        NotificationPanel panel = new NotificationPanel("+" + bonus);
        notificationContainer.getChildren().add(panel);
        panel.showScore(notificationContainer.getChildren());
    }

    /**
     * Display a custom message notification.
     * 
     * @param message the message to display
     */
    public void showMessage(String message) {
        if (notificationContainer == null) {
            return;
        }
        
        NotificationPanel panel = new NotificationPanel(message);
        notificationContainer.getChildren().add(panel);
        panel.showScore(notificationContainer.getChildren());
    }

    public void showLineClearReward(int lines) {
        if (lines <= 0 || notificationContainer == null) return;

        String text = "";
        String styleClass = "";
        double scale = 1.0;

        switch (lines) {
            case 1: text = "SINGLE"; styleClass = "reward-single"; scale = 1.0; break;
            case 2: text = "DOUBLE"; styleClass = "reward-double"; scale = 1.2; break;
            case 3: text = "TRIPLE"; styleClass = "reward-triple"; scale = 1.4; break;
            case 4: text = "TETRIS!"; styleClass = "reward-tetris"; scale = 1.8; break;
            default: return;
        }

        Text rewardText = new Text(text);
        rewardText.getStyleClass().add(styleClass);
        
        notificationContainer.getChildren().add(rewardText);

        SequentialTransition sequence = new SequentialTransition();

        ScaleTransition pop = new ScaleTransition(Duration.millis(200), rewardText);
        pop.setFromX(0); pop.setFromY(0);
        pop.setToX(scale); pop.setToY(scale);
        pop.setInterpolator(Interpolator.EASE_OUT);

        PauseTransition hang = new PauseTransition(Duration.millis(800));

        ParallelTransition floatFade = new ParallelTransition();
        TranslateTransition moveUp = new TranslateTransition(Duration.millis(300), rewardText);
        moveUp.setByY(-50);
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), rewardText);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        floatFade.getChildren().addAll(moveUp, fadeOut);

        sequence.getChildren().addAll(pop, hang, floatFade);
        sequence.setOnFinished(e -> notificationContainer.getChildren().remove(rewardText));
        sequence.play();
        
        if (lines == 4) {
            triggerScreenShake();
        }
    }

    private void triggerScreenShake() {
        // Simple shake effect on the container itself
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), notificationContainer);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }
}
