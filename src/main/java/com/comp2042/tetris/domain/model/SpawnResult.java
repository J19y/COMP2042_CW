package com.comp2042.tetris.domain.model;

/**
 * Simple result object indicating whether a brick spawn resulted in game over.
 * <p>
 * When a new brick spawns and immediately collides with existing blocks,
 * the game is over as no more moves are possible.
 * </p>
 *
 * @version 1.0
 */
public final class SpawnResult {
    private final boolean gameOver;

    /**
     * Constructs a SpawnResult indicating the spawn outcome.
     *
     * @param gameOver true if the spawn resulted in game over
     */
    public SpawnResult(boolean gameOver) {
        this.gameOver = gameOver;
    }


    /**
     * Checks if the spawn resulted in game over.
     *
     * @return {@code true} if game over, {@code false} if spawn was successful
     */
    public boolean isGameOver() {
        return gameOver;
    }
}

