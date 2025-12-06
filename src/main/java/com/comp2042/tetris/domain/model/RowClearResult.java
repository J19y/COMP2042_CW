package com.comp2042.tetris.domain.model;

import java.util.Collections;
import java.util.List;

import com.comp2042.tetris.util.MatrixOperations;

/**
 * Result object returned after row clearing operations.
 * <p>
 * Contains information about how many lines were removed, the updated board matrix,
 * score bonus earned, and indices of the cleared rows for animation purposes.
 * </p>
 *
 * @version 1.0
 */
public final class RowClearResult {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;
    private final List<Integer> clearedRows;

    /**
     * Constructs a RowClearResult with all clearing information.
     *
     * @param linesRemoved the number of lines that were cleared
     * @param newMatrix the board matrix after clearing
     * @param scoreBonus bonus points earned from the clear
     * @param clearedRows list of row indices that were cleared
     */
    public RowClearResult(int linesRemoved, int[][] newMatrix, int scoreBonus, List<Integer> clearedRows) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.clearedRows = clearedRows == null ? Collections.emptyList() : Collections.unmodifiableList(clearedRows);
    }

    /**
     * Gets the number of lines that were removed.
     *
     * @return the count of cleared lines
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Returns a defensive copy of the board matrix after clearing.
     *
     * @return a copy of the updated matrix
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Gets the bonus score earned from the line clear.
     *
     * @return the score bonus
     */
    public int getScoreBonus() {
        return scoreBonus;
    }

    /**
     * Gets the list of row indices that were cleared.
     * <p>
     * Used for triggering row clear animations.
     * </p>
     *
     * @return an unmodifiable list of cleared row indices
     */
    public List<Integer> getClearedRows() {
        return clearedRows;
    }
}
