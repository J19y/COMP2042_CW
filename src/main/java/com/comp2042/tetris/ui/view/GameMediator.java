package com.comp2042.tetris.ui.view;

import com.comp2042.tetris.app.GameLoopController;
import com.comp2042.tetris.domain.model.RowClearResult;
import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.mechanics.state.GameStateManager;
import com.comp2042.tetris.services.notify.NotificationManager;
import com.comp2042.tetris.ui.render.ActiveBrickRenderer;
import com.comp2042.tetris.ui.render.BoardRenderer;

import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

// Mediates interactions between UI components and game state/rendering logic.
final class GameMediator {

    private final BoardRenderer boardRenderer;
    private final ViewInitializer viewInitializer;
    private final GameStateManager stateManager;
    private final GridPane gamePanel;
    private final GameOverPanel gameOverPanel;

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
        if (clear != null && clear.getLinesRemoved() > 0) {
            if (notificationService != null) {
                notificationService.showScoreBonus(clear.getScoreBonus());
            }
            refreshGameBackground(clear.getNewMatrix());
        }
        if (activeBrickRenderer != null && data.getViewData() != null && stateManager.canUpdateGame()) {
            activeBrickRenderer.refresh(data.getViewData());
        }
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
        if (gameOverPanel != null) {
            gameOverPanel.show();
        }
        stateManager.gameOver();
    }

    void prepareNewGame() {
        if (gameLoopController != null) {
            gameLoopController.stop();
        }
        if (gameOverPanel != null) {
            gameOverPanel.hide();
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
