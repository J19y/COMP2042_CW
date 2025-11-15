package com.comp2042.tetris.domain.model;

// Represents the result of attempting to spawn a new brick on the board.
public final class SpawnResult {
    private final boolean gameOver;

    public SpawnResult(boolean gameOver) {
        this.gameOver = gameOver;
    }


    // Indicates if the spawn resulted in game over.
    public boolean isGameOver() {
        return gameOver;
    }
}
