package com.comp2042.tetris.application.session;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Encapsulates the JavaFX Timeline-based game tick mechanism.
 * <p>
 * Supports dynamic interval adjustment for speed changes during gameplay.
 * The loop runs indefinitely until stopped, calling the provided callback
 * on each tick.
 * </p>
 *
 * @version 1.0
 */
public final class GameLoop {
    private final Timeline timeline;
    private final Runnable onTick;
    private javafx.util.Duration interval;

    /**
     * Constructs a GameLoop with the specified interval and tick callback.
     *
     * @param interval the duration between ticks
     * @param onTick the callback to invoke on each tick
     */
    public GameLoop(Duration interval, Runnable onTick) {
        this.onTick = onTick;
        this.interval = interval;
        this.timeline = new Timeline(new KeyFrame(interval, _ -> this.onTick.run()));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Changes the tick interval dynamically.
     * <p>
     * If the loop is running, it will be restarted with the new interval.
     * </p>
     *
     * @param newInterval the new duration between ticks
     */
    public void setInterval(Duration newInterval) {
        boolean running = isRunning();
        timeline.stop();
        this.interval = newInterval;
        this.timeline.getKeyFrames().clear();
        this.timeline.getKeyFrames().add(new KeyFrame(newInterval, _ -> this.onTick.run()));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        if (running) timeline.play();
    }

    /**
     * Gets the current tick interval.
     *
     * @return the current interval duration
     */
    public Duration getInterval() {
        return interval;
    }

    /**
     * Starts the game loop.
     */
    public void start() {
        timeline.play();
    }

    /**
     * Stops the game loop.
     */
    public void stop() {
        timeline.stop();
    }

    /**
     * Checks if the game loop is currently running.
     *
     * @return {@code true} if running, {@code false} otherwise
     */
    public boolean isRunning() {
        return timeline.getStatus() == Timeline.Status.RUNNING;
    }
}

