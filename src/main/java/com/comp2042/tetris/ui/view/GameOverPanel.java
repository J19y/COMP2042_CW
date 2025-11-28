package com.comp2042.tetris.ui.view;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GameOverPanel extends BorderPane {

    private final Label gameOverLabel;
    private final Label scoreLabel;
    private final Button retryButton;
    private final Button menuButton;
    
    private Runnable retryAction = () -> {};
    private Runnable returnAction = () -> {};

    public GameOverPanel() {
        gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("game-over-text");
        scoreLabel = new Label("Final Score: 0");
        scoreLabel.getStyleClass().add("game-over-score");
        // Try to load and apply the AXR ArcadeMachine font for the header and final score
        try {
            javafx.scene.text.Font arcadeFontSmall = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/AXR ArcadeMachine.ttf"), 28);
            javafx.scene.text.Font arcadeFontLarge = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/AXR ArcadeMachine.ttf"), 120);
            // Prefer Press Start 2P for the score if available
            javafx.scene.text.Font pressStartFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-vaV7.ttf"), 36);

            // Diagnostic output so we can see which fonts loaded when running the app
            System.out.println("GameOverPanel: pressStartFont=" + (pressStartFont == null ? "null" : pressStartFont.getFamily())
                    + ", arcadeFontSmall=" + (arcadeFontSmall == null ? "null" : arcadeFontSmall.getFamily())
                    + ", arcadeFontLarge=" + (arcadeFontLarge == null ? "null" : arcadeFontLarge.getFamily()));

            // Prefer Press Start 2P for the final score text, fallback to arcade font family when unavailable.
            try {
                if (pressStartFont != null) {
                    scoreLabel.setFont(pressStartFont);
                } else if (arcadeFontSmall != null) {
                    scoreLabel.setFont(arcadeFontSmall);
                }
                // Force an inline style so CSS won't accidentally override the family/size at runtime
                String scoreFamily = (pressStartFont != null) ? pressStartFont.getFamily() : (arcadeFontSmall == null ? "Arial" : arcadeFontSmall.getFamily());
                try {
                    scoreLabel.setStyle("-fx-font-family: '" + scoreFamily + "'; -fx-font-size: 18px;");
                } catch (Exception ignored) {
                }
            } catch (Exception e) {
                System.err.println("GameOverPanel: failed to set score label font/style - " + e.getMessage());
            }

            if (arcadeFontLarge != null) {
                gameOverLabel.setFont(arcadeFontLarge);
                // Also set inline style to ensure CSS doesn't override the font-family/size
                try {
                    gameOverLabel.setStyle("-fx-font-family: '" + arcadeFontLarge.getFamily() + "'; -fx-font-size: 120px;");
                } catch (Exception ignored) {
                    // ignore style setting errors
                }
            }
        } catch (Exception ignored) {
            // fallback to default system fonts if loading fails
        }

        // Use full-labeled menu-style buttons for consistency with settings/menu UI
            retryButton = new Button("↺ RETRY");
            menuButton = new Button("⏏ MAIN MENU");
            // Apply base menu-button style and a specific modifier class for per-button colors
            retryButton.getStyleClass().addAll("menu-button", "retry-button");
            menuButton.getStyleClass().addAll("menu-button", "mainmenu-button");
        // Set a sensible preferred width so they match other menu buttons
        retryButton.setPrefWidth(180);
        menuButton.setPrefWidth(180);
        retryButton.setDisable(true);
        menuButton.setDisable(true);

        retryButton.setOnAction(e -> {
            try { com.comp2042.tetris.services.audio.MusicManager.getInstance().playSfx("/audio/ButtonClickingEffect.mp3"); } catch (Exception ignored) {}
            hide();
            retryAction.run();
        });
        menuButton.setOnAction(e -> {
            try { com.comp2042.tetris.services.audio.MusicManager.getInstance().playSfx("/audio/ButtonClickingEffect.mp3"); } catch (Exception ignored) {}
            hide();
            returnAction.run();
        });

        HBox actions = new HBox(12, retryButton, menuButton);
        actions.setAlignment(Pos.CENTER);
        actions.getStyleClass().add("game-over-actions");

        VBox content = new VBox(18, gameOverLabel, scoreLabel, actions);
        content.setAlignment(Pos.CENTER);
        setCenter(content);
        getStyleClass().add("game-over-panel");
        setVisible(false);

        // We'll animate children individually in show(); keep initial state prepared
        resetLabelState();
    }
    
    public void show(int finalScore) {
        scoreLabel.setText("Final Score: " + finalScore);
        super.setVisible(true);
        retryButton.setDisable(false);
        menuButton.setDisable(false);

        // Reset positions for animated entrance
        gameOverLabel.setTranslateY(-50); // Start slightly above
        gameOverLabel.setOpacity(0);
        gameOverLabel.setScaleX(2.0); // Start huge
        gameOverLabel.setScaleY(2.0);
        
        scoreLabel.setOpacity(0);
        scoreLabel.setTranslateY(20);
        
        // Assume the HBox containing buttons is the 3rd child of the VBox
        javafx.scene.Node buttonBox = ((VBox)this.getCenter()).getChildren().get(2);
        buttonBox.setOpacity(0);
        buttonBox.setTranslateY(20);

        // 1. Slam the Game Over Text
        Timeline slam = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(gameOverLabel.opacityProperty(), 0),
                new KeyValue(gameOverLabel.translateYProperty(), -100),
                new KeyValue(gameOverLabel.scaleXProperty(), 3.0),
                new KeyValue(gameOverLabel.scaleYProperty(), 3.0)
            ),
            new KeyFrame(Duration.millis(300),
                new KeyValue(gameOverLabel.opacityProperty(), 1),
                new KeyValue(gameOverLabel.translateYProperty(), 0, javafx.animation.Interpolator.EASE_IN),
                new KeyValue(gameOverLabel.scaleXProperty(), 1.0, javafx.animation.Interpolator.EASE_IN),
                new KeyValue(gameOverLabel.scaleYProperty(), 1.0, javafx.animation.Interpolator.EASE_IN)
            )
        );

        // 2. Fade/Slide in Score (Delayed)
        FadeTransition fadeScore = new FadeTransition(Duration.millis(400), scoreLabel);
        fadeScore.setFromValue(0); 
        fadeScore.setToValue(1);
        
        TranslateTransition slideScore = new TranslateTransition(Duration.millis(400), scoreLabel);
        slideScore.setToY(0);
        
        ParallelTransition showScore = new ParallelTransition(fadeScore, slideScore);
        showScore.setDelay(Duration.millis(300)); // Wait for slam

        // 3. Fade/Slide in Buttons (Delayed more)
        FadeTransition fadeButtons = new FadeTransition(Duration.millis(400), buttonBox);
        fadeButtons.setFromValue(0); 
        fadeButtons.setToValue(1);

        TranslateTransition slideButtons = new TranslateTransition(Duration.millis(400), buttonBox);
        slideButtons.setToY(0);

        ParallelTransition showButtons = new ParallelTransition(fadeButtons, slideButtons);
        showButtons.setDelay(Duration.millis(500));

        // Play all
        new ParallelTransition(slam, showScore, showButtons).play();
    }
    
    public void hide() {
        super.setVisible(false);
        resetLabelState();
        retryButton.setDisable(true);
        menuButton.setDisable(true);
    }
    private void resetLabelState() {
        gameOverLabel.setScaleX(1);
        gameOverLabel.setScaleY(1);
        gameOverLabel.setOpacity(1);
        gameOverLabel.setTranslateY(0);
        gameOverLabel.setTranslateX(0);
        gameOverLabel.setText("GAME OVER");
        scoreLabel.setText("Final Score: 0");
    }

    public void setOnRetry(Runnable retryAction) {
        this.retryAction = retryAction != null ? retryAction : () -> {};
    }

    public void setOnReturnToMenu(Runnable returnAction) {
        this.returnAction = returnAction != null ? returnAction : () -> {};
    }

}
