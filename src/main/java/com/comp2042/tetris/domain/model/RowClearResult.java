package com.comp2042.tetris.domain.model;

import com.comp2042.tetris.util.MatrixOperations;
import java.util.Collections;
import java.util.List;

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
    private final List<Integer> clearedRows;

    public RowClearResult(int linesRemoved, int[][] newMatrix, int scoreBonus, List<Integer> clearedRows) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.clearedRows = clearedRows == null ? Collections.emptyList() : Collections.unmodifiableList(clearedRows);
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

    /**
     * Returns an unmodifiable list of row indices that were cleared (0-based, top->bottom).
     */
    public List<Integer> getClearedRows() {
        return clearedRows;
    }
}