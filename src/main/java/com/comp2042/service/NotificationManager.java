package com.comp2042.service;

import com.comp2042.ui.NotificationPanel;

import javafx.scene.Group;

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
}
