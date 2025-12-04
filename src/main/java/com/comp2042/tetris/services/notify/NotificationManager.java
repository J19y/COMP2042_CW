package com.comp2042.tetris.services.notify;

import com.comp2042.tetris.ui.view.NotificationPanel;
import com.comp2042.tetris.ui.view.RowClearMessage;

import javafx.application.Platform;
import javafx.scene.Group;


public final class NotificationManager {
    
    private final Group notificationContainer;
    private NotificationPanel notificationPanel;
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(NotificationManager.class.getName());

    
    public NotificationManager(Group notificationContainer) {
        this.notificationContainer = notificationContainer;
        
        if (this.notificationContainer != null) {
            this.notificationPanel = new NotificationPanel();
            
            
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

    
    public void showEventMessage(String message) {
        if (notificationContainer == null) return;
        if (message == null || message.isEmpty()) return;
        System.out.println("NotificationManager: showEventMessage -> " + message);
        Platform.runLater(() -> RowClearMessage.showCustom(notificationContainer, message));
    }

    public void showLineClearReward(int lines) {
        if (notificationContainer == null || lines <= 0) {
            return;
        }
        RowClearMessage.show(notificationContainer, lines);
        LOGGER.fine(() -> "Displayed row clear message for " + lines + " lines.");
    }

    
}

