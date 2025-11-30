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
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GameOverPanel extends BorderPane {

    private final Label gameOverLabel;
    private final Label scoreLabel;
    private final Label levelLabel;
    private final Label statsLabel;
    private final Button retryButton;
    private final Button menuButton;
    private final Rectangle glowRect;
    
    private Runnable retryAction = () -> {};
    private Runnable returnAction = () -> {};

    public GameOverPanel() {
        gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("game-over-text");
        scoreLabel = new Label("Final Score: 0");
        scoreLabel.getStyleClass().add("game-over-score");
        levelLabel = new Label();
        levelLabel.getStyleClass().add("game-over-level");
        levelLabel.setVisible(false);
        statsLabel = new Label("Lines Cleared: 0\nTime Played: 00:00");
        statsLabel.getStyleClass().add("game-over-stats");
        
        // Add a glowing background rectangle for extra visual impact
        glowRect = new Rectangle();
        glowRect.getStyleClass().add("game-over-glow");
        glowRect.setManaged(false);
        glowRect.setMouseTransparent(true);
        
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
                    statsLabel.setFont(javafx.scene.text.Font.font(pressStartFont.getFamily(), 24));
                    levelLabel.setFont(javafx.scene.text.Font.font(pressStartFont.getFamily(), 20));
                } else if (arcadeFontSmall != null) {
                    scoreLabel.setFont(arcadeFontSmall);
                    statsLabel.setFont(javafx.scene.text.Font.font(arcadeFontSmall.getFamily(), 20));
                    levelLabel.setFont(javafx.scene.text.Font.font(arcadeFontSmall.getFamily(), 18));
                }
                // Force an inline style so CSS won't accidentally override the family/size at runtime
                String scoreFamily = (pressStartFont != null) ? pressStartFont.getFamily() : (arcadeFontSmall == null ? "Arial" : arcadeFontSmall.getFamily());
                try {
                    scoreLabel.setStyle("-fx-font-family: '" + scoreFamily + "'; -fx-font-size: 18px;");
                    statsLabel.setStyle("-fx-font-family: '" + scoreFamily + "'; -fx-font-size: 14px;");
                    levelLabel.setStyle("-fx-font-family: '" + scoreFamily + "'; -fx-font-size: 16px; -fx-text-fill: #ffd966;");
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

        VBox content = new VBox(18, gameOverLabel, scoreLabel, levelLabel, statsLabel, actions);
        content.setAlignment(Pos.CENTER);
        
        // Add the glow rectangle as background
        getChildren().add(glowRect);
        setCenter(content);
        getStyleClass().add("game-over-panel");
        setVisible(false);

        // We'll animate children individually in show(); keep initial state prepared
        resetLabelState();
    }
    
    public void show(int finalScore, int linesCleared, long timePlayedMillis) {
        scoreLabel.setText("Final Score: " + finalScore);
        // Calculate actual stats
        int minutes = (int) (timePlayedMillis / 60000);
        int seconds = (int) ((timePlayedMillis % 60000) / 1000);
        statsLabel.setText(String.format("Lines Cleared: %d\nTime Played: %02d:%02d", linesCleared, minutes, seconds));
        super.setVisible(true);
        retryButton.setDisable(false);
        menuButton.setDisable(false);

        // Position and size the glow rectangle to match the panel
        glowRect.setWidth(getWidth() - 20);
        glowRect.setHeight(getHeight() - 20);
        glowRect.setX(10);
        glowRect.setY(10);
        glowRect.setOpacity(0);

        // Reset positions for animated entrance
        gameOverLabel.setTranslateY(-50); // Start slightly above
        gameOverLabel.setOpacity(0);
        gameOverLabel.setScaleX(2.0); // Start huge
        gameOverLabel.setScaleY(2.0);
        
        scoreLabel.setOpacity(0);
        scoreLabel.setTranslateY(20);
        
        levelLabel.setOpacity(0);
        levelLabel.setTranslateY(20);
        // If a mystery level was set earlier, ensure the label is visible for the entrance animation
        if (levelLabel.getText() != null && !levelLabel.getText().isEmpty()) {
            levelLabel.setVisible(true);
        }

        statsLabel.setOpacity(0);
        statsLabel.setTranslateY(20);
        
        // Assume the HBox containing buttons is the 4th child of the VBox
        javafx.scene.Node buttonBox = ((VBox)this.getCenter()).getChildren().get(3);
        buttonBox.setOpacity(0);
        buttonBox.setTranslateY(20);

        // 1. Slam the Game Over Text
        Timeline slam = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(gameOverLabel.opacityProperty(), 0),
                new KeyValue(gameOverLabel.translateYProperty(), -100),
                new KeyValue(gameOverLabel.scaleXProperty(), 3.0),
                new KeyValue(gameOverLabel.scaleYProperty(), 3.0),
                new KeyValue(glowRect.opacityProperty(), 0)
            ),
            new KeyFrame(Duration.millis(300),
                new KeyValue(gameOverLabel.opacityProperty(), 1),
                new KeyValue(gameOverLabel.translateYProperty(), 0, javafx.animation.Interpolator.EASE_IN),
                new KeyValue(gameOverLabel.scaleXProperty(), 1.0, javafx.animation.Interpolator.EASE_IN),
                new KeyValue(gameOverLabel.scaleYProperty(), 1.0, javafx.animation.Interpolator.EASE_IN),
                new KeyValue(glowRect.opacityProperty(), 0.6)
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

        // 3. Fade/Slide in Stats (Delayed more)
        FadeTransition fadeStats = new FadeTransition(Duration.millis(400), statsLabel);
        fadeStats.setFromValue(0); 
        fadeStats.setToValue(1);
        
        TranslateTransition slideStats = new TranslateTransition(Duration.millis(400), statsLabel);
        slideStats.setToY(0);
        
        // Fade and slide in the level label (explicit from/to values)
        FadeTransition fadeLevel = new FadeTransition(Duration.millis(360), levelLabel);
        fadeLevel.setFromValue(0);
        fadeLevel.setToValue(1);
        TranslateTransition slideLevel = new TranslateTransition(Duration.millis(360), levelLabel);
        slideLevel.setToY(0);
        ParallelTransition showLevel = new ParallelTransition(fadeLevel, slideLevel);
        showLevel.setDelay(Duration.millis(360));

        ParallelTransition showStats = new ParallelTransition(fadeStats, slideStats);
        showStats.setDelay(Duration.millis(420));
        showStats.setDelay(Duration.millis(400));

        // 4. Fade/Slide in Buttons (Delayed more)
        FadeTransition fadeButtons = new FadeTransition(Duration.millis(400), buttonBox);
        fadeButtons.setFromValue(0); 
        fadeButtons.setToValue(1);

        TranslateTransition slideButtons = new TranslateTransition(Duration.millis(400), buttonBox);
        slideButtons.setToY(0);

        ParallelTransition showButtons = new ParallelTransition(fadeButtons, slideButtons);
        showButtons.setDelay(Duration.millis(500));

        // Play all
        new ParallelTransition(slam, showScore, showLevel, showStats, showButtons).play();
        
        // Add a subtle pulsing glow animation
        Timeline glowPulse = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(glowRect.opacityProperty(), 0.6)),
            new KeyFrame(Duration.millis(2000), new KeyValue(glowRect.opacityProperty(), 0.3)),
            new KeyFrame(Duration.millis(4000), new KeyValue(glowRect.opacityProperty(), 0.6))
        );
        glowPulse.setCycleCount(Timeline.INDEFINITE);
        glowPulse.setDelay(Duration.millis(800));
        glowPulse.play();
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
        levelLabel.setText("");
        levelLabel.setVisible(false);
        statsLabel.setText("Lines Cleared: 0\nTime Played: 00:00");
        statsLabel.setOpacity(1);
        statsLabel.setTranslateY(0);
        glowRect.setOpacity(0);
    }

    /**
     * If running a mystery game, this method sets the final level to display on the Game Over panel.
     * Use a negative value or 0 to hide the level (default behaviour for non-mystery modes).
     */
    public void setMysteryLevel(int level) {
        if (level <= 0) {
            levelLabel.setVisible(false);
            levelLabel.setText("");
            return;
        }
        levelLabel.setText("Final Level: " + level);
        levelLabel.setVisible(true);
    }

    public void setOnRetry(Runnable retryAction) {
        this.retryAction = retryAction != null ? retryAction : () -> {};
    }

    public void setOnReturnToMenu(Runnable returnAction) {
        this.returnAction = returnAction != null ? returnAction : () -> {};
    }

}
