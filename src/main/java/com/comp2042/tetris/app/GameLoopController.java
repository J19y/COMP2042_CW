package com.comp2042.tetris.app;

import javafx.util.Duration;

/**
 * Manages the game loop lifecycle.
 * Extracted from GuiController to follow Single Responsibility Principle.
 * Responsible only for starting, stopping, and checking the game loop state.
 */
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
}
