package com.comp2042.tetris.ui.view;

import com.comp2042.tetris.app.GameLoopController;
import com.comp2042.tetris.domain.model.RowClearResult;
import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.mechanics.state.GameStateManager;
import com.comp2042.tetris.services.notify.NotificationManager;
import com.comp2042.tetris.ui.render.ActiveBrickRenderer;
import com.comp2042.tetris.ui.render.BoardRenderer;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.effect.Glow;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

// Mediates interactions between UI components and game state/rendering logic.
final class GameMediator {

    private final BoardRenderer boardRenderer;
    private final ViewInitializer viewInitializer;
    private final GameStateManager stateManager;
    private final GridPane gamePanel;
    private final GameOverPanel gameOverPanel;
    private final GameOverAnimator gameOverAnimator;

    private Rectangle[][] displayMatrix;
    private ActiveBrickRenderer activeBrickRenderer;
    private NotificationManager notificationService;
    private GameLoopController gameLoopController;

    GameMediator(BoardRenderer boardRenderer,
                       ViewInitializer viewInitializer,
                       GameStateManager stateManager,
                       GridPane gamePanel,
                       GameOverPanel gameOverPanel) {
        this.boardRenderer = boardRenderer;
        this.viewInitializer = viewInitializer;
        this.stateManager = stateManager;
        this.gamePanel = gamePanel;
        this.gameOverPanel = gameOverPanel;
        this.gameOverAnimator = new GameOverAnimator(gamePanel, gameOverPanel);
    }

    void configureVisuals(Rectangle[][] displayMatrix,
                          ActiveBrickRenderer activeBrickRenderer,
                          NotificationManager notificationService) {
        this.displayMatrix = displayMatrix;
        this.activeBrickRenderer = activeBrickRenderer;
        this.notificationService = notificationService;
    }

    void setGameLoop(GameLoopController controller) {
        this.gameLoopController = controller;
    }

    void refreshGameBackground(int[][] boardMatrix) {
        if (boardRenderer != null && displayMatrix != null) {
            boardRenderer.refreshBoard(boardMatrix, displayMatrix);
        }
    }

    void handleResult(ShowResult data) {
        if (data == null) {
            return;
        }
        RowClearResult clear = data.getClearRow();
        if (clear != null) {
            playLockEffects();
            
            if (clear.getLinesRemoved() > 0) {
                if (notificationService != null) {
                    notificationService.showScoreBonus(clear.getScoreBonus());
                    notificationService.showLineClearReward(clear.getLinesRemoved());
                }
                refreshGameBackground(clear.getNewMatrix());
            }
        }
        if (activeBrickRenderer != null && data.getViewData() != null && stateManager.canUpdateGame()) {
            activeBrickRenderer.refresh(data.getViewData());
        }
    }

    private void playLockEffects() {
        if (gamePanel == null) return;

        // A. Squash and Stretch (The "Thud")
        ScaleTransition squash = new ScaleTransition(Duration.millis(50), gamePanel);
        squash.setToY(0.98);
        squash.setToX(1.02);
        squash.setCycleCount(2);
        squash.setAutoReverse(true);
        squash.play();

        // B. Light Flash (Bloom)
        Glow glow = new Glow(0.8);
        gamePanel.setEffect(glow);
        
        Timeline flash = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0.8)),
            new KeyFrame(Duration.millis(150), new KeyValue(glow.levelProperty(), 0.0))
        );
        flash.setOnFinished(e -> gamePanel.setEffect(null));
        flash.play();
    }

    void focusGamePanel() {
        if (viewInitializer != null) {
            viewInitializer.requestFocus(gamePanel);
        }
    }

    void handleGameOver() {
        if (gameLoopController != null) {
            gameLoopController.stop();
        }
        stateManager.gameOver();
        
        // Trigger the "Digital Fragmentation" sequence
        if (gameOverAnimator != null) {
            gameOverAnimator.playDigitalFragmentationSequence(displayMatrix);
        }
    }

    void prepareNewGame() {
        if (gameLoopController != null) {
            gameLoopController.stop();
        }
        if (gameOverPanel != null) {
            gameOverPanel.hide();
        }
        // Reset board visibility
        if (displayMatrix != null) {
            for (Rectangle[] row : displayMatrix) {
                for (Rectangle rect : row) {
                    if (rect != null) rect.setVisible(true);
                }
            }
        }
    }

    void ensureLoopRunning() {
        if (gameLoopController != null && !gameLoopController.isRunning()) {
            gameLoopController.start();
        }
    }

    void togglePause() {
        if (stateManager.isPaused()) {
            stateManager.resumeGame();
            if (gameLoopController != null && !gameLoopController.isRunning()) {
                gameLoopController.start();
            }
        } else if (stateManager.canUpdateGame()) {
            stateManager.pauseGame();
            if (gameLoopController != null && gameLoopController.isRunning()) {
                gameLoopController.stop();
            }
        }
        focusGamePanel();
    }
}
