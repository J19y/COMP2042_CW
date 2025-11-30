package com.comp2042.tetris.domain.model;

import com.comp2042.tetris.util.MatrixOperations;
import java.util.Collections;
import java.util.List;


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

    
    public List<Integer> getClearedRows() {
        return clearedRows;
    }
}
