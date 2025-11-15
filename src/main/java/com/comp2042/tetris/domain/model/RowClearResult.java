package com.comp2042.tetris.domain.model;

import com.comp2042.tetris.util.MatrixOperations;

/**
 * Represents the result of clearing completed rows from the game board.
 * Contains the number of lines removed, the new board state, and the score bonus.
 * 
 * Renamed from ClearRow to RowClearResult to better indicate this is a result object
 * that holds the outcome of the row clearing operation, not the operation itself.
 */
public final class RowClearResult {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    public RowClearResult(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    public int getScoreBonus() {
        return scoreBonus;
    }
}