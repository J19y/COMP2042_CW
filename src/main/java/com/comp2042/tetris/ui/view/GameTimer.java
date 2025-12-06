package com.comp2042.tetris.ui.view;

import com.comp2042.tetris.services.audio.MusicManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages game timer display with count-up and countdown modes.
 * Integrates with BackgroundAnimator for visual effects.
 * Supports low-time warnings with flicker effects for timed modes.
 *
 */
public class GameTimer {

    private static final Logger LOGGER = Logger.getLogger(GameTimer.class.getName());

    private final Text timerText;
    private final BackgroundAnimator backgroundAnimator;

    private long startTime;
    private long accumulatedNanos;
    private boolean timerRunning;
    private boolean countdownMode;

    private Timeline lowTimeFlicker;
    private boolean lowTimeActive;

    public GameTimer(Text timerText, Pane backgroundPane) {
        this.timerText = timerText;
        this.backgroundAnimator = backgroundPane == null ? null : new BackgroundAnimator(backgroundPane, () -> updateTimer(System.nanoTime()));
        initializeState();
    }

    public void startAnimation() {
        resetTimerTracking();
        if (backgroundAnimator != null) {
            backgroundAnimator.start();
        }
    }

    public void stopAnimation() {
        if (backgroundAnimator != null) {
            backgroundAnimator.stop();
        }
    }

    public void resetTimerTracking() {
        initializeState();
    }

    private void initializeState() {
        accumulatedNanos = 0L;
        startTime = System.nanoTime();
        timerRunning = false;
        countdownMode = false;
        updateTimerDisplay(0L);
    }

    public void pauseTimerTracking() {
        if (timerRunning) {
            accumulatedNanos += System.nanoTime() - startTime;
            timerRunning = false;
        }
    }

    public void resumeTimerTracking() {
        startTime = System.nanoTime();
        timerRunning = true;
    }

    public void setCountdownMode(boolean countdownMode) {
        this.countdownMode = countdownMode;
    }

    public boolean isCountdownMode() {
        return countdownMode;
    }

    public void setRemainingTime(int seconds) {
        countdownMode = true;
        if (timerText == null) return;
        int minutes = Math.max(0, seconds / 60);
        int secs = Math.max(0, seconds % 60);
        final String text = String.format("%02d:%02d", minutes, secs);
        Platform.runLater(() -> {
            timerText.setText(text);
            if (seconds <= 10 && seconds > 0) {
                activateLowTimeFlicker();
                playLowTimeSfx();
            } else if (seconds <= 0) {
                deactivateLowTimeFlicker();
                stopLowTimeSfx();
            } else {
                deactivateLowTimeFlicker();
            }
        });
    }

    public void clearLowTimeEffects() {
        deactivateLowTimeFlicker();
        stopLowTimeSfx();
    }

    private void activateLowTimeFlicker() {
        if (lowTimeActive || timerText == null) {
            return;
        }
        lowTimeActive = true;
        timerText.setFill(Color.RED);
        lowTimeFlicker = new Timeline(
            new KeyFrame(Duration.millis(450), ev -> timerText.setVisible(false)),
            new KeyFrame(Duration.millis(900), ev -> timerText.setVisible(true))
        );
        lowTimeFlicker.setCycleCount(Timeline.INDEFINITE);
        lowTimeFlicker.play();
    }

    private void deactivateLowTimeFlicker() {
        if (!lowTimeActive) {
            return;
        }
        lowTimeActive = false;
        if (lowTimeFlicker != null) {
            lowTimeFlicker.stop();
            lowTimeFlicker = null;
        }
        if (timerText != null) {
            timerText.setVisible(true);
            timerText.setFill(Color.WHITE);
        }
    }

    private void playLowTimeSfx() {
        try {
            MusicManager.getInstance().playSfxAtVolume("/audio/10SecondsTimer.mp3", 0.95);
        } catch (Exception ignored) {
            LOGGER.log(Level.FINE, "Unable to play low-time sfx", ignored);
        }
    }

    private void stopLowTimeSfx() {
        try {
            MusicManager.getInstance().stopSfx("/audio/10SecondsTimer.mp3");
        } catch (Exception ignored) {
            LOGGER.log(Level.FINE, "Unable to stop low-time sfx", ignored);
        }
    }

    private void updateTimer(long now) {
        if (timerText == null || countdownMode) {
            return;
        }
        long totalNanos = accumulatedNanos;
        if (timerRunning) {
            totalNanos += now - startTime;
        }
        updateTimerDisplay(totalNanos);
    }

    private void updateTimerDisplay(long totalNanos) {
        if (timerText == null) {
            return;
        }
        long elapsedSeconds = totalNanos / 1_000_000_000L;
        long minutes = elapsedSeconds / 60;
        long seconds = elapsedSeconds % 60;
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }
}
