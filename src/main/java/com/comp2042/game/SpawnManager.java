package com.comp2042.game;

/**
 * Handles spawning of new bricks and game-over detection logic.
 */
public final class SpawnManager {
    private final Board board;

    public interface GameOverCallback {
        void onGameOver();
    }

    public SpawnManager(Board board) {
        this.board = board;
    }

    /**
     * Attempts to spawn a new brick. If spawn indicates collision (game over), invokes callback.
     * Returns true if game over was triggered.
     */
    public boolean spawn(GameOverCallback callback) {
        boolean gameOver = board.spawnBrick();
        if (gameOver && callback != null) {
            callback.onGameOver();
        }
        return gameOver;
    }
}
