package com.comp2042.tetris.services.notify;

import com.comp2042.tetris.ui.view.RowClearMessage;
import javafx.scene.Group;

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
        if (notificationContainer == null || lines <= 0) {
            return;
        }
        RowClearMessage.show(notificationContainer, lines);
        LOGGER.fine(() -> "Displayed row clear message for " + lines + " lines.");
    }

    
}
