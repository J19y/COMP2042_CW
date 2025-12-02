package com.comp2042.tetris.application.session;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


public final class GameLoop {
    private Timeline timeline;
    private final Runnable onTick;
    private javafx.util.Duration interval;

    public GameLoop(Duration interval, Runnable onTick) {
        this.onTick = onTick;
        this.interval = interval;
        this.timeline = new Timeline(new KeyFrame(interval, _ -> this.onTick.run()));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void setInterval(Duration newInterval) {
        boolean running = isRunning();
        timeline.stop();
        this.interval = newInterval;
        this.timeline.getKeyFrames().clear();
        this.timeline.getKeyFrames().add(new KeyFrame(newInterval, _ -> this.onTick.run()));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        if (running) timeline.play();
    }

    public Duration getInterval() {
        return interval;
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

