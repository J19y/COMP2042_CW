package com.comp2042.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Simple wrapper around a JavaFX Timeline to drive the game tick.
 * Extracted from GuiController to isolate timing concerns.
 */
public final class GameLoop {
    private final Timeline timeline;

    public GameLoop(Duration interval, Runnable onTick) {
        this.timeline = new Timeline(new KeyFrame(interval, _ -> onTick.run()));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void start() {
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }

    public boolean isRunning() {
        return timeline.getStatus() == Timeline.Status.RUNNING;
    }
}
