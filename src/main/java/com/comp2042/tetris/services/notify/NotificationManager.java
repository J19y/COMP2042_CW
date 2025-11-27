package com.comp2042.tetris.services.notify;

import com.comp2042.tetris.ui.view.NotificationPanel;
import com.comp2042.tetris.ui.view.RowClearMessage;
import javafx.application.Platform;
import javafx.scene.Group;

/**
 * Service responsible for displaying notification messages in the game.
 * Extracted from GuiController to follow Single Responsibility Principle.
 */
public final class NotificationManager {
    
    private final Group notificationContainer;
    private final NotificationPanel notificationPanel;
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(NotificationManager.class.getName());

    /**
     * Creates a new NotificationService.
     * 
     * @param notificationContainer the Group that will contain notification panels
     */
    public NotificationManager(Group notificationContainer) {
        this.notificationContainer = notificationContainer;
        // create and attach the notification panel to the provided group
        if (this.notificationContainer != null) {
            notificationPanel = new NotificationPanel();
            // If we're on the FX thread, add it synchronously to avoid scheduling delays;
            // otherwise queue it to the FX thread.
            if (Platform.isFxApplicationThread()) {
                if (!this.notificationContainer.getChildren().contains(notificationPanel)) {
                    this.notificationContainer.getChildren().add(notificationPanel);
                }
            } else {
                Platform.runLater(() -> {
                    if (!this.notificationContainer.getChildren().contains(notificationPanel)) {
                        this.notificationContainer.getChildren().add(notificationPanel);
                    }
                });
            }
        } else {
            notificationPanel = null;
        }
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
        if (notificationPanel == null) {
            LOGGER.fine(() -> "Score panel missing: +" + bonus);
            return;
        }
        final String text = "+" + bonus;
        Platform.runLater(() -> notificationPanel.showScore(text));
    }

    /**
     * Show score bonus at a vertical offset (translateY) inside the notification panel.
     * This allows callers to position the +score near other notifications (e.g. below a row-clear message).
     */
    public void showScoreBonus(int bonus, double offsetY) {
        if (notificationContainer == null) {
            return;
        }
        if (notificationPanel == null) {
            LOGGER.fine(() -> "Score panel missing: +" + bonus);
            return;
        }
        final String text = "+" + bonus;
        Platform.runLater(() -> notificationPanel.showScore(text, offsetY));
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
        if (notificationPanel == null) {
            LOGGER.fine(() -> "Message suppressed (no panel): " + message);
            return;
        }
        final String text = message;
        Platform.runLater(() -> notificationPanel.showScore(text));
    }

    public void showLineClearReward(int lines) {
        if (notificationContainer == null || lines <= 0) {
            return;
        }
        RowClearMessage.show(notificationContainer, lines);
        LOGGER.fine(() -> "Displayed row clear message for " + lines + " lines.");
    }

    
}
