package com.comp2042.tetris.util;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import com.comp2042.tetris.domain.model.RowClearResult;

/**
 * Utility class providing static methods for matrix operations.
 * <p>
 * Contains core algorithms for matrix manipulation including copying,
 * merging brick shapes onto the board, clearing completed rows, and
 * deep copying lists of matrices.
 * </p>
 * 
 * @version 1.0
 */
public class MatrixOperations {
    
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private MatrixOperations(){
    }

    /**
     * Creates a deep copy of a 2D integer array.
     *
     * @param original the original matrix to copy
     * @return a new matrix with the same values as the original
     */
    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    /**
     * Merges a brick shape onto the board matrix at the specified position.
     *
     * @param filledFields the current board matrix
     * @param brick the brick shape matrix to merge
     * @param x the x-coordinate (column) for placement
     * @param y the y-coordinate (row) for placement
     * @return a new matrix with the brick merged at the specified position
     */
    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + j;
                int targetY = y + i;
                if (brick[i][j] != 0) {
                    if (targetY >= 0 && targetY < copy.length && targetX >= 0 && targetX < copy[targetY].length) {
                        copy[targetY][targetX] = brick[i][j];
                    }
                }
            }
        }
        return copy;
    }

    /**
     * Clears all completed rows from the matrix and shifts remaining rows down.
     * <p>
     * A row is considered complete when all cells contain non-zero values.
     * Cleared rows are removed and empty rows are added at the top.
     * </p>
     *
     * @param matrix the board matrix to check and clear
     * @return a {@link RowClearResult} containing the updated matrix and clear statistics
     */
    public static RowClearResult clearRows(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            int[] tmpRow = new int[matrix[i].length];
            boolean rowToClear = true;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                }
                tmpRow[j] = matrix[i][j];
            }
            if (rowToClear) {
                clearedRows.add(i);
            } else {
                newRows.add(tmpRow);
            }
        }
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) {
                tmp[i] = row;
            } else {
                break;
            }
        }
        
        return new RowClearResult(clearedRows.size(), tmp, 0, clearedRows);
    }

    /**
     * Creates a deep copy of a list of 2D integer arrays.
     *
     * @param list the list of matrices to copy
     * @return a new list with deep copies of all matrices
     */
    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }
}
