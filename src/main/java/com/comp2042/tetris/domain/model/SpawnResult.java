package com.comp2042.tetris.domain.model;


public final class SpawnResult {
    private final boolean gameOver;

    public SpawnResult(boolean gameOver) {
        this.gameOver = gameOver;
    }


    
    public boolean isGameOver() {
        return gameOver;
    }
}

