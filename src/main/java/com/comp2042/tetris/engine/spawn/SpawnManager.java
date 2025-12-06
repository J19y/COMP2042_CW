package com.comp2042.tetris.engine.spawn;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages brick spawning and game over notification.
 * <p>
 * Implements the Observer pattern to notify registered listeners
 * when a spawn results in game over (collision at spawn position).
 * </p>
 *
 * @version 1.0
 */
public final class SpawnManager {
    private final BrickSpawn spawner;

    /**
     * Callback interface for game over notifications.
     */
    public interface GameOverCallback {
        /**
         * Called when game over is detected.
         */
        void onGameOver();
    }

    private final List<GameOverCallback> observers = new ArrayList<>();

    /**
     * Constructs a SpawnManager with the given brick spawner.
     *
     * @param spawner the brick spawn implementation to use
     */
    public SpawnManager(BrickSpawn spawner) {
        this.spawner = spawner;
    }

    /**
     * Registers a game over observer.
     *
     * @param listener the callback to register
     */
    public void addGameOverObserver(GameOverCallback listener) {
        if (listener != null && !observers.contains(listener)) {
            observers.add(listener);
        }
    }

    /**
     * Removes a game over observer.
     *
     * @param listener the callback to remove
     */
    public void removeGameOverObserver(GameOverCallback listener) {
        observers.remove(listener);
    }

    /**
     * Spawns a new brick and checks for game over.
     *
     * @param callback optional callback for immediate game over notification
     * @return {@code true} if game over occurred, {@code false} otherwise
     */
    public boolean spawn(GameOverCallback callback) {
        boolean gameOver = spawner.spawnBrick().isGameOver();
        if (gameOver) {
            if (callback != null) {
                callback.onGameOver();
            }
            
            if (!observers.isEmpty()) {
                
                List<GameOverCallback> snapshot = new ArrayList<>(observers);
                for (GameOverCallback l : snapshot) {
                    try {
                        l.onGameOver();
                    } catch (RuntimeException ignored) {
                        
                    }
                }
            }
        }
        return gameOver;
    }

    /**
     * Spawns a new brick without a specific callback.
     * <p>
     * Registered observers will still be notified on game over.
     * </p>
     *
     * @return {@code true} if game over occurred, {@code false} otherwise
     */
    public boolean spawn() {
        return spawn(null);
    }
}

