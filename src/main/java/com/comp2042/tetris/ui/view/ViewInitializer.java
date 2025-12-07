package com.comp2042.tetris.ui.view;

import javafx.scene.layout.GridPane;

/**
 * Initializes UI components and loads custom fonts.
 * Sets up game panel focus and game over panel visibility.
 */
public final class ViewInitializer {

    private String digitalFontFamily;

    public void loadFonts(Class<?> loaderClass) {
        try {
            java.net.URL res = loaderClass.getClassLoader().getResource("fonts/digital.ttf");
            if (res != null) {
                javafx.scene.text.Font f = javafx.scene.text.Font.loadFont(res.toExternalForm(), 38);
                if (f != null) {
                    digitalFontFamily = f.getFamily();
                }
            }
        } catch (Exception ignored) {
            
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

