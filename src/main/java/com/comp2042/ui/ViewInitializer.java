package com.comp2042.ui;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 * Extracted from GuiController to follow Single Responsibility Principle.
 * Responsible only for initial view configuration (fonts, focus, visibility).
 */
public final class ViewInitializer {


    public void loadFonts(Class<?> loaderClass) {
        Font.loadFont(loaderClass.getClassLoader()
            .getResource("digital.ttf").toExternalForm(), 38);
    }


    public void setupGamePanel(GridPane gamePanel) {
        if (gamePanel != null) {
            gamePanel.setFocusTraversable(true);
            gamePanel.requestFocus();
        }
    }


    public void setupGameOverPanel(GameOverPanel gameOverPanel) {
        if (gameOverPanel != null) {
            gameOverPanel.setVisible(false);
        }
    }

 
    public void requestFocus(GridPane gamePanel) {
        if (gamePanel != null) {
            gamePanel.requestFocus();
        }
    }
}
