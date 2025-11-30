package com.comp2042.tetris.app;

import javafx.util.Duration;


public final class GameLoopController {
    
    private final GameLoop gameLoop;


    public GameLoopController(Duration tickInterval, Runnable onTick) {
        this.gameLoop = new GameLoop(tickInterval, onTick);
    }


    public void start() {
        if (gameLoop != null) {
            gameLoop.start();
        }
    }


    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }


    public void restart() {
        stop();
        start();
    }


    public boolean isRunning() {
        return gameLoop != null && gameLoop.isRunning();
    }

    public void setInterval(Duration interval) {
        if (gameLoop != null) {
            gameLoop.setInterval(interval);
        }
    }

    public Duration getInterval() {
        return gameLoop == null ? null : gameLoop.getInterval();
    }
}

