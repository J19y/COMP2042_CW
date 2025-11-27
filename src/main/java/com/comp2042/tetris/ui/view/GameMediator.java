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
import java.util.List;

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
    // Keep a copy of the last known board matrix so we can animate diffs
    private int[][] lastBoardMatrix;

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

    /**
     * Animate the active brick settling before the engine merges it to the board.
     */
    void settleActiveBrick(Runnable onFinished) {
        if (activeBrickRenderer == null) {
            if (onFinished != null) onFinished.run();
            return;
        }
        activeBrickRenderer.animateSettle(onFinished);
    }

    void setGameLoop(GameLoopController controller) {
        this.gameLoopController = controller;
    }

    void refreshGameBackground(int[][] boardMatrix) {
        if (boardRenderer == null || displayMatrix == null) return;

        // If we don't have a previous matrix, simply refresh and store
        if (lastBoardMatrix == null) {
            boardRenderer.refreshBoard(boardMatrix, displayMatrix);
            lastBoardMatrix = copyMatrix(boardMatrix);
            return;
        }

        // First update visuals so rectangles have the latest colors/effects
        boardRenderer.refreshBoard(boardMatrix, displayMatrix);

        // Find cells that transitioned from empty (0) -> filled (non-zero)
        javafx.collections.ObservableList<javafx.animation.Animation> animations = javafx.collections.FXCollections.observableArrayList();
        for (int i = 0; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                int prev = lastBoardMatrix[i][j];
                int now = boardMatrix[i][j];
                if (prev == 0 && now != 0) {
                    Rectangle rect = displayMatrix[i][j];
                    if (rect == null) continue;

                    // Pop + settle animation: small upward offset then drop into place
                    rect.setTranslateY(-10);
                    javafx.animation.TranslateTransition tt = new javafx.animation.TranslateTransition(javafx.util.Duration.millis(160), rect);
                    tt.setFromY(-10);
                    tt.setToY(0);
                    tt.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

                    javafx.animation.ScaleTransition st = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(160), rect);
                    st.setFromX(0.90);
                    st.setFromY(0.90);
                    st.setToX(1.0);
                    st.setToY(1.0);
                    st.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

                    // Neon pulse: animate DropShadow radius/spread if present
                    javafx.animation.Timeline effectPulse = null;
                    javafx.scene.effect.Effect eff = rect.getEffect();
                    if (eff instanceof javafx.scene.effect.DropShadow) {
                        javafx.scene.effect.DropShadow ds = (javafx.scene.effect.DropShadow) eff;
                        double baseRadius = ds.getRadius();
                        double baseSpread = ds.getSpread();

                        effectPulse = new javafx.animation.Timeline(
                            new javafx.animation.KeyFrame(javafx.util.Duration.ZERO,
                                new javafx.animation.KeyValue(ds.radiusProperty(), baseRadius),
                                new javafx.animation.KeyValue(ds.spreadProperty(), baseSpread)
                            ),
                            new javafx.animation.KeyFrame(javafx.util.Duration.millis(80),
                                new javafx.animation.KeyValue(ds.radiusProperty(), Math.min(baseRadius + 8, baseRadius * 2 + 6)),
                                new javafx.animation.KeyValue(ds.spreadProperty(), Math.min(baseSpread + 0.25, 1.0))
                            ),
                            new javafx.animation.KeyFrame(javafx.util.Duration.millis(160),
                                new javafx.animation.KeyValue(ds.radiusProperty(), baseRadius),
                                new javafx.animation.KeyValue(ds.spreadProperty(), baseSpread)
                            )
                        );
                    }

                    // Stroke width pulse for extra neon pop
                    javafx.animation.Timeline strokePulse = new javafx.animation.Timeline(
                        new javafx.animation.KeyFrame(javafx.util.Duration.ZERO,
                            new javafx.animation.KeyValue(rect.strokeWidthProperty(), rect.getStrokeWidth())),
                        new javafx.animation.KeyFrame(javafx.util.Duration.millis(80),
                            new javafx.animation.KeyValue(rect.strokeWidthProperty(), rect.getStrokeWidth() + 1.5)),
                        new javafx.animation.KeyFrame(javafx.util.Duration.millis(160),
                            new javafx.animation.KeyValue(rect.strokeWidthProperty(), rect.getStrokeWidth()))
                    );

                    javafx.animation.ParallelTransition pt = new javafx.animation.ParallelTransition(tt, st);
                    if (effectPulse != null) pt.getChildren().add(effectPulse);
                    pt.getChildren().add(strokePulse);

                    // Stagger by row so locks feel weighty (lower rows animate slightly sooner)
                    int rows = boardMatrix.length;
                    long delay = Math.max(0, (rows - i) * 10 + j * 8);
                    pt.setDelay(javafx.util.Duration.millis(delay));
                    animations.add(pt);
                }
            }
        }

        // Play a short board-wide neon bloom while blocks lock
        javafx.animation.Timeline boardBloom = null;
        if (!animations.isEmpty() && gamePanel != null) {
            javafx.scene.effect.Effect prev = gamePanel.getEffect();
            javafx.scene.effect.Glow glow = new javafx.scene.effect.Glow(0.0);
            gamePanel.setEffect(glow);
            boardBloom = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.ZERO, new javafx.animation.KeyValue(glow.levelProperty(), 0.0)),
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(90), new javafx.animation.KeyValue(glow.levelProperty(), 0.75)),
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(220), new javafx.animation.KeyValue(glow.levelProperty(), 0.0))
            );
            boardBloom.setOnFinished(e -> gamePanel.setEffect(prev));
        }

        // Play all animations together for a cohesive lock effect
        if (!animations.isEmpty()) {
            javafx.animation.ParallelTransition all = new javafx.animation.ParallelTransition();
            all.getChildren().addAll(animations);
            if (boardBloom != null) {
                all.getChildren().add(boardBloom);
            }
            all.play();
        }

        // Update last known matrix
        lastBoardMatrix = copyMatrix(boardMatrix);
    }

    private int[][] copyMatrix(int[][] src) {
        int[][] copy = new int[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, copy[i], 0, src[i].length);
        }
        return copy;
    }

    void handleResult(ShowResult data) {
        if (data == null) {
            return;
        }
        RowClearResult clear = data.getClearRow();
        if (clear != null) {
            // Show notifications immediately as soon as the clear is detected
            if (clear.getLinesRemoved() > 0) {
                if (notificationService != null) {
                    // Show the descriptive row-clear message first (top-centered)
                    notificationService.showLineClearReward(clear.getLinesRemoved());
                    // Show the +score slightly below the message so they feel connected
                    // Offset chosen to sit just below the row-clear title when the title uses translateY=52
                    notificationService.showScoreBonus(clear.getScoreBonus(), 96.0);
                }
            }

            // Play lock effects and sweep/refresh the board
            playLockEffects();
            if (clear.getLinesRemoved() > 0) {
                // Play neon sweep across cleared rows for visual feedback
                playNeonSweep(clear.getClearedRows());
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

    private void playNeonSweep(List<Integer> clearedRows) {
        if (clearedRows == null || clearedRows.isEmpty() || displayMatrix == null) return;

        javafx.animation.ParallelTransition all = new javafx.animation.ParallelTransition();

        // Sweep each cleared row left->right
        for (int rowIdx : clearedRows) {
            if (rowIdx < 0 || rowIdx >= displayMatrix.length) continue;
            Rectangle[] row = displayMatrix[rowIdx];
            if (row == null) continue;

            // Slight offset so multi-row clears cascade visually
            double rowOffset = (clearedRows.size() - clearedRows.indexOf(rowIdx)) * 30.0;

            for (int col = 0; col < row.length; col++) {
                Rectangle rect = row[col];
                if (rect == null) continue;

                final double originalStrokeWidth = rect.getStrokeWidth();
                final javafx.scene.paint.Paint originalStroke = rect.getStroke();
                final javafx.scene.effect.Effect originalEffect = rect.getEffect();

                double delayMs = rowOffset + col * 28.0;

                javafx.animation.Timeline tl = new javafx.animation.Timeline();

                // At start: apply neon drop shadow and change stroke color
                javafx.animation.KeyFrame kfStart = new javafx.animation.KeyFrame(javafx.util.Duration.millis(delayMs), e -> {
                    javafx.scene.effect.DropShadow ds = new javafx.scene.effect.DropShadow();
                    ds.setColor(javafx.scene.paint.Color.web("#6EE7B7"));
                    ds.setRadius(6);
                    ds.setSpread(0.35);
                    rect.setEffect(ds);
                    rect.setStroke(javafx.scene.paint.Color.web("#E6FFFA"));
                });

                // Peak: increase stroke width
                javafx.animation.KeyFrame kfPeak = new javafx.animation.KeyFrame(javafx.util.Duration.millis(delayMs + 120),
                    new javafx.animation.KeyValue(rect.strokeWidthProperty(), originalStrokeWidth + 2.2)
                );

                // Fade back to original
                javafx.animation.KeyFrame kfEnd = new javafx.animation.KeyFrame(javafx.util.Duration.millis(delayMs + 300), e -> {
                    rect.setStroke(originalStroke);
                    rect.setStrokeWidth(originalStrokeWidth);
                    rect.setEffect(originalEffect);
                });

                tl.getKeyFrames().addAll(kfStart, kfPeak, kfEnd);
                all.getChildren().add(tl);
            }
        }

        all.play();
    }

    void focusGamePanel() {
        if (viewInitializer != null) {
            viewInitializer.requestFocus(gamePanel);
        }
    }

    void handleGameOver(int finalScore) {
        if (gameLoopController != null) {
            gameLoopController.stop();
        }
        stateManager.gameOver();
        
        // Trigger the "Digital Fragmentation" sequence
        if (gameOverAnimator != null) {
            gameOverAnimator.playDigitalFragmentationSequence(displayMatrix, finalScore);
        }
    }

    void prepareNewGame() {
        if (gameLoopController != null) {
            gameLoopController.stop();
        }
        if (gameOverPanel != null) {
            gameOverPanel.hide();
        }
        if (gameOverAnimator != null) {
            gameOverAnimator.resetBackdropEffects();
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
