package com.comp2042.tetris.ui.view;

import com.comp2042.tetris.application.port.CreateNewGame;
import com.comp2042.tetris.application.port.GameModeLifecycle;
import com.comp2042.tetris.engine.state.GameStateManager;
import com.comp2042.tetris.services.audio.MusicManager;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Interpolator;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the 3-2-1 countdown animation before game starts.
 * Coordinates with game state and audio for synchronized start.
 */
public class CountdownManager {

    private static final Logger LOGGER = Logger.getLogger(CountdownManager.class.getName());

    private final StackPane countdownOverlay;
    private final Text countdownText;
    private final GameStateManager stateManager;
    private final GameMediator mediator;
    private final PauseOverlayController pauseOverlayController;
    private final GameTimer timer;

    private CreateNewGame gameLifecycle;

    public CountdownManager(StackPane countdownOverlay, Text countdownText,
                            GameStateManager stateManager, GameMediator mediator,
                            PauseOverlayController pauseOverlayController, GameTimer timer) {
        this.countdownOverlay = countdownOverlay;
        this.countdownText = countdownText;
        this.stateManager = stateManager;
        this.mediator = mediator;
        this.pauseOverlayController = pauseOverlayController;
        this.timer = timer;
    }

    public void setGameLifecycle(CreateNewGame gameLifecycle) {
        this.gameLifecycle = gameLifecycle;
    }

    public void startCountdown() {
        if (countdownOverlay == null) {
            return;
        }
        pauseOverlayController.setManualPauseActive(false);
        timer.pauseTimerTracking();
        timer.resetTimerTracking();

        countdownOverlay.setVisible(true);
        if (countdownText != null) {
            countdownText.setScaleX(0);
            countdownText.setScaleY(0);
            countdownText.setOpacity(0);
        }
        if (stateManager != null && !stateManager.isPaused()) {
            stateManager.pauseGame();
        }

        SequenceFactory sequenceFactory = new SequenceFactory();
        SequentialTransition sequence = new SequentialTransition(
            sequenceFactory.animateNumber("3"),
            new PauseTransition(Duration.millis(100)),
            sequenceFactory.animateNumber("2"),
            new PauseTransition(Duration.millis(100)),
            sequenceFactory.animateNumber("1"),
            new PauseTransition(Duration.millis(100)),
            sequenceFactory.animateNumber("GO")
        );

        final MusicManager mm = MusicManager.getInstance();
        final double originalMusicVol = safeGetMusicVolume(mm, 1.0);
        final double duckedVol = Math.max(0.05, originalMusicVol * 0.25);
        try {
            mm.fadeMusicTo(duckedVol, 160);
        } catch (Exception ignored) {
            LOGGER.log(Level.FINE, "Unable to duck music", ignored);
        }

        sequence.setOnFinished(e -> {
            try {
                mm.fadeMusicTo(originalMusicVol, 1200);
            } catch (Exception ignored) {
                LOGGER.log(Level.FINE, "Unable to restore music", ignored);
            }
            countdownOverlay.setVisible(false);
            if (stateManager != null) {
                stateManager.resumeGame();
            }
            if (mediator != null) {
                mediator.ensureLoopRunning();
                mediator.focusGamePanel();
            }
            timer.resumeTimerTracking();
            timer.setCountdownMode(false);
            if (gameLifecycle instanceof GameModeLifecycle) {
                try {
                    ((GameModeLifecycle) gameLifecycle).startMode();
                } catch (Exception ignored) {
                    LOGGER.log(Level.FINE, "Unable to start mode after countdown", ignored);
                }
            }
        });
        sequence.play();
    }

    private double safeGetMusicVolume(MusicManager mm, double fallback) {
        if (mm == null) {
            return fallback;
        }
        try {
            return mm.getMusicVolume();
        } catch (Exception ignored) {
            return fallback;
        }
    }

    private class SequenceFactory {
        private ParallelTransition animateNumber(String text) {
            ParallelTransition parallel = new ParallelTransition();
            PauseTransition pre = new PauseTransition(Duration.ZERO);
            pre.setOnFinished(ev -> {
                if (countdownText != null) {
                    countdownText.setText(text);
                    if ("3".equals(text)) {
                        try {
                            MusicManager.getInstance().playSfxImmediate("/audio/321GOEffect.mp3");
                        } catch (Exception ignored) {
                            LOGGER.log(Level.FINE, "Unable to play countdown sfx", ignored);
                        }
                    }
                }
            });
            ScaleTransition scale = new ScaleTransition(Duration.millis(400), countdownText);
            scale.setFromX(0.0);
            scale.setFromY(0.0);
            scale.setToX(1.5);
            scale.setToY(1.5);
            scale.setInterpolator(Interpolator.EASE_OUT);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), countdownText);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), countdownText);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setDelay(Duration.millis(600));
            parallel.getChildren().addAll(pre, scale, fadeIn, fadeOut);
            return parallel;
        }
    }
}
