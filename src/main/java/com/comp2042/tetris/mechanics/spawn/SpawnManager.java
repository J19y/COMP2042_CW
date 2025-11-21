package com.comp2042.tetris.mechanics.spawn;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles spawning of new bricks and game-over detection logic.
 * Now also acts as a simple Subject (Observer pattern) for game-over events.
 */
public final class SpawnManager {
    private final BrickSpawn spawner;

    public interface GameOverCallback {
        void onGameOver();
    }

    // Minimal observer list to avoid introducing many new classes
    private final List<GameOverCallback> observers = new ArrayList<>();

    public SpawnManager(BrickSpawn spawner) {
        this.spawner = spawner;
    }

    
    // Register a listener for game-over events.
    public void addGameOverObserver(GameOverCallback listener) {
        if (listener != null && !observers.contains(listener)) {
            observers.add(listener);
        }
    }

    
    // Unregister a listener for game-over events.
    public void removeGameOverObserver(GameOverCallback listener) {
        observers.remove(listener);
    }

    /**
     * Attempts to spawn a new brick. If spawn indicates collision (game over), invokes callback
     * and notifies all registered observers. Returns true if game over was triggered.
     */
    public boolean spawn(GameOverCallback callback) {
        boolean gameOver = spawner.spawnBrick().isGameOver();
        if (gameOver) {
            if (callback != null) {
                callback.onGameOver();
            }
            // notify observers
            if (!observers.isEmpty()) {
                // iterate over a copy to avoid ConcurrentModification if observers change during notification
                List<GameOverCallback> snapshot = new ArrayList<>(observers);
                for (GameOverCallback l : snapshot) {
                    try {
                        l.onGameOver();
                    } catch (RuntimeException ignored) {
                        // Keep other observers running; log could be added if logging available
                    }
                }
            }
        }
        return gameOver;
    }

    
    // Convenience overload when no direct callback is needed.
     
    public boolean spawn() {
        return spawn(null);
    }
}
