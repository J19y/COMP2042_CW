package com.comp2042.tetris.ui.view;

import java.util.List;

import com.comp2042.tetris.application.session.GameLoopController;
import com.comp2042.tetris.domain.model.RowClearResult;
import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.engine.state.GameStateManager;
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

/**
 * Mediator coordinating UI components during gameplay.
 * Manages board rendering, animations, and game state transitions.
 */
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

        
        if (lastBoardMatrix == null) {
            boardRenderer.refreshBoard(boardMatrix, displayMatrix);
            lastBoardMatrix = copyMatrix(boardMatrix);
            return;
        }

        
        boardRenderer.refreshBoard(boardMatrix, displayMatrix);

        
        javafx.collections.ObservableList<javafx.animation.Animation> animations = javafx.collections.FXCollections.observableArrayList();
        for (int i = 0; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                int prev = lastBoardMatrix[i][j];
                int now = boardMatrix[i][j];
                if (prev == 0 && now != 0) {
                    Rectangle rect = displayMatrix[i][j];
                    if (rect == null) continue;

                    
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

                    
                    javafx.animation.Timeline effectPulse = null;
                    javafx.scene.effect.Effect eff = rect.getEffect();
                    if (eff instanceof javafx.scene.effect.DropShadow ds) {
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

                    
                    int rows = boardMatrix.length;
                    long delay = Math.max(0, (rows - i) * 10 + j * 8);
                    pt.setDelay(javafx.util.Duration.millis(delay));
                    animations.add(pt);
                }
            }
        }

        
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

        
        if (!animations.isEmpty()) {
            javafx.animation.ParallelTransition all = new javafx.animation.ParallelTransition();
            all.getChildren().addAll(animations);
            if (boardBloom != null) {
                all.getChildren().add(boardBloom);
            }
            all.play();
        }

        
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
            
            if (clear.getLinesRemoved() > 0) {
                if (notificationService != null) {
                    
                    notificationService.showLineClearReward(clear.getLinesRemoved());
                    
                    
                    notificationService.showScoreBonus(clear.getScoreBonus(), 96.0);
                }
            }

            
            playLockEffects();
            if (clear.getLinesRemoved() > 0) {
                
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

        
        ScaleTransition squash = new ScaleTransition(Duration.millis(50), gamePanel);
        squash.setToY(0.98);
        squash.setToX(1.02);
        squash.setCycleCount(2);
        squash.setAutoReverse(true);
        squash.play();

        
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

        
        for (int rowIdx : clearedRows) {
            if (rowIdx < 0 || rowIdx >= displayMatrix.length) continue;
            Rectangle[] row = displayMatrix[rowIdx];
            if (row == null) continue;

            
            double rowOffset = (clearedRows.size() - clearedRows.indexOf(rowIdx)) * 30.0;

            for (int col = 0; col < row.length; col++) {
                Rectangle rect = row[col];
                if (rect == null) continue;

                final double originalStrokeWidth = rect.getStrokeWidth();
                final javafx.scene.paint.Paint originalStroke = rect.getStroke();
                final javafx.scene.effect.Effect originalEffect = rect.getEffect();

                double delayMs = rowOffset + col * 28.0;

                javafx.animation.Timeline tl = new javafx.animation.Timeline();

                
                javafx.animation.KeyFrame kfStart = new javafx.animation.KeyFrame(javafx.util.Duration.millis(delayMs), e -> {
                    javafx.scene.effect.DropShadow ds = new javafx.scene.effect.DropShadow();
                    ds.setColor(javafx.scene.paint.Color.web("#6EE7B7"));
                    ds.setRadius(6);
                    ds.setSpread(0.35);
                    rect.setEffect(ds);
                    rect.setStroke(javafx.scene.paint.Color.web("#E6FFFA"));
                });

                
                javafx.animation.KeyFrame kfPeak = new javafx.animation.KeyFrame(javafx.util.Duration.millis(delayMs + 120),
                    new javafx.animation.KeyValue(rect.strokeWidthProperty(), originalStrokeWidth + 2.2)
                );

                
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

    void handleGameOver(int finalScore, int totalLines, long gameTime) {
        if (gameLoopController != null) {
            gameLoopController.stop();
        }
        stateManager.gameOver();
        
        
        if (gameOverAnimator != null) {
            gameOverAnimator.playDigitalFragmentationSequence(displayMatrix, finalScore, totalLines, gameTime);
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

