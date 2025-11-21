package com.comp2042.tetris.util;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.comp2042.tetris.domain.model.RowClearResult;

class MatrixOperationsTest {

    @Test
    void testCopy_createsIndependentCopy() {
        // Create a simple 2x2 matrix
        int[][] original = {
            {1, 2},
            {3, 4}
        };

        // Make a copy and modify it
        int[][] copied = MatrixOperations.copy(original);
        copied[0][0] = 99;

        // Original should remain unchanged
        assertEquals(1, original[0][0]);
        assertEquals(99, copied[0][0]);
    }

    @Test
    void testCopy_handlesEmptyMatrix() {
        // Edge case: empty matrix
        int[][] empty = new int[0][0];
        int[][] copied = MatrixOperations.copy(empty);
        
        assertEquals(0, copied.length);
    }

    // Copy should work for non-rectangular (jagged) matrices too.
    @Test
    void testCopy_handlesJaggedRows() {
        int[][] jagged = {
            {1},
            {2, 3, 4}
        };

        int[][] copied = MatrixOperations.copy(jagged);
        copied[1][2] = 99;

        assertEquals(4, jagged[1][2]);
        assertEquals(99, copied[1][2]);
    }

    @Test
    void testMerge_overlaysBrickOntoBoard() {
        // Simple 3x3 board with one cell filled
        int[][] board = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        
        // 2x2 brick to place at position (1, 0)
        int[][] brick = {
            {2, 2},
            {2, 0}
        };

        int[][] result = MatrixOperations.merge(board, brick, 1, 0);

        // Check that brick values appear in the right spots
        assertEquals(2, result[0][1]);
        assertEquals(2, result[0][2]);
        assertEquals(2, result[1][1]);
        // Original board value should be overwritten where brick is non-zero
        assertEquals(2, result[1][1]);
    }

    @Test
    void testMerge_doesNotModifyOriginalBoard() {
        // Make sure the original board stays untouched
        int[][] board = {
            {0, 0},
            {0, 0}
        };
        int[][] brick = {
            {1, 1}
        };

        MatrixOperations.merge(board, brick, 0, 0);

        // Original board should still be all zeros
        assertEquals(0, board[0][0]);
        assertEquals(0, board[0][1]);
    }

    // Zero entries in the brick must leave existing board cells untouched.
    @Test
    void testMerge_preservesExistingCellsWhenBrickHasZeros() {
        int[][] board = {
            {5, 5},
            {0, 0}
        };
        int[][] brick = {
            {0, 0},
            {6, 0}
        };

        int[][] merged = MatrixOperations.merge(board, brick, 0, 0);

        assertEquals(5, merged[0][0]);
        assertEquals(5, merged[0][1]);
        assertEquals(6, merged[1][0]);
    }

    @Test
    void testClearRows_removesFullRows() {
        // Create a matrix where rows 0 and 2 are completely filled
        int[][] matrix = {
            {1, 1, 1},  // Full row
            {0, 1, 0},  // Partial row
            {2, 2, 2}   // Full row
        };

        RowClearResult result = MatrixOperations.clearRows(matrix);

        // Should detect 2 cleared rows
        assertEquals(2, result.getLinesRemoved());
        
        // The new matrix should have the partial row at the bottom
        int[][] newMatrix = result.getNewMatrix();
        assertEquals(0, newMatrix[2][0]);
        assertEquals(1, newMatrix[2][1]);
        assertEquals(0, newMatrix[2][2]);
        
        // Top rows should be empty now
        assertEquals(0, newMatrix[0][0]);
        assertEquals(0, newMatrix[0][1]);
    }

    @Test
    void testClearRows_handlesNoFullRows() {
        // Matrix with no complete rows
        int[][] matrix = {
            {1, 0, 1},
            {0, 1, 0},
            {1, 0, 1}
        };

        RowClearResult result = MatrixOperations.clearRows(matrix);

        // No rows should be removed
        assertEquals(0, result.getLinesRemoved());
    }

    // Clearing every row should yield an empty matrix of zeros.
    @Test
    void testClearRows_allRowsClearedLeavesEmptyMatrix() {
        int[][] matrix = {
            {1, 1},
            {1, 1}
        };

        RowClearResult result = MatrixOperations.clearRows(matrix);

        assertEquals(2, result.getLinesRemoved());
        int[][] newMatrix = result.getNewMatrix();
        assertArrayEquals(new int[] {0, 0}, newMatrix[0]);
        assertArrayEquals(new int[] {0, 0}, newMatrix[1]);
    }

    // The input matrix should remain unchanged after row clearing logic.
    @Test
    void testClearRows_doesNotMutateOriginalMatrix() {
        int[][] matrix = {
            {1, 1, 1},
            {0, 1, 0}
        };

        RowClearResult result = MatrixOperations.clearRows(matrix);

        assertEquals(1, result.getLinesRemoved());
        assertEquals(1, matrix[1][1]);
    }

    @Test
    void testDeepCopyList_copiesAllMatrices() {
        // Create a list with a couple of matrices
        int[][] matrix1 = {{1, 2}, {3, 4}};
        int[][] matrix2 = {{5, 6}, {7, 8}};
        
        List<int[][]> original = new ArrayList<>();
        original.add(matrix1);
        original.add(matrix2);

        // Deep copy the list
        List<int[][]> copied = MatrixOperations.deepCopyList(original);

        // Modify the copied version
        copied.get(0)[0][0] = 99;

        // Original should be unchanged
        assertEquals(1, original.get(0)[0][0]);
        assertEquals(99, copied.get(0)[0][0]);
    }

    @Test
    void testDeepCopyList_handlesEmptyList() {
        // Edge case: empty list
        List<int[][]> empty = new ArrayList<>();
        List<int[][]> copied = MatrixOperations.deepCopyList(empty);
        
        assertTrue(copied.isEmpty());
    }
}
