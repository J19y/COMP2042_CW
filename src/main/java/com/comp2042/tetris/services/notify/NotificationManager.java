package com.comp2042.tetris.services.notify;


import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import com.comp2042.tetris.ui.view.LineClearNotification;
import javafx.util.Duration;

/**
 * Service responsible for displaying notification messages in the game.
 * Extracted from GuiController to follow Single Responsibility Principle.
 */
public final class NotificationManager {
    
    private final Group notificationContainer;
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(NotificationManager.class.getName());

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
        // Notifications disabled: old NotificationPanel removed
        LOGGER.fine(() -> "Score bonus suppressed: +" + bonus);
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
        // Notifications disabled: old NotificationPanel removed
        LOGGER.fine(() -> "Message suppressed: " + message);
    }

    public void showLineClearReward(int lines) {
        if (lines <= 0 || notificationContainer == null) return;
        String title;
        Color accent;
        switch (lines) {
            case 1: title = "SINGLE"; accent = Color.web("#6EE7B7"); break;
            case 2: title = "DOUBLE"; accent = Color.web("#60A5FA"); break;
            case 3: title = "TRIPLE"; accent = Color.web("#FDBA74"); break;
            case 4: title = "TETRIS!"; accent = Color.web("#FB7185"); break;
            default: return;
        }

        LOGGER.info(() -> "Line clear: " + lines + " lines. Showing reward: " + title);
        LineClearNotification panel = new LineClearNotification(title, null, accent);
        panel.show(notificationContainer);

        if (lines == 4) {
            triggerScreenShake();
            LineClearNotification.spawnConfetti(notificationContainer);
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
