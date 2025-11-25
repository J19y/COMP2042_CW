package com.comp2042.tetris.ui.view;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 * Extracted from GuiController to follow Single Responsibility Principle.
 * Responsible only for initial view configuration (fonts, focus, visibility).
 */
public final class ViewInitializer {

    private String digitalFontFamily;

    public void loadFonts(Class<?> loaderClass) {
        try {
            java.net.URL res = loaderClass.getClassLoader().getResource("digital.ttf");
            if (res != null) {
                javafx.scene.text.Font f = javafx.scene.text.Font.loadFont(res.toExternalForm(), 38);
                if (f != null) {
                    digitalFontFamily = f.getFamily();
                }
            }
        } catch (Exception ignored) {
            // ignore font loading failures; fallbacks will be used
        }
    }

    public String getDigitalFontFamily() {
        return digitalFontFamily;
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
