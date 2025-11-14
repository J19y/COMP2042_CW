package com.comp2042.game;

/**
 * Handles spawning of new bricks and game-over detection logic.
 */
public final class SpawnManager {
    private final BrickSpawn spawner;

    public interface GameOverCallback {
        void onGameOver();
    }

    public SpawnManager(BrickSpawn spawner) {
        this.spawner = spawner;
    }

    /**
     * Attempts to spawn a new brick. If spawn indicates collision (game over), invokes callback.
     * Returns true if game over was triggered.
     */
    public boolean spawn(GameOverCallback callback) {
        boolean gameOver = spawner.spawnBrick().isGameOver();
        if (gameOver && callback != null) {
            callback.onGameOver();
        }
        return gameOver;
    }
}
