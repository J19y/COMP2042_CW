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
        
        int[][] original = {
            {1, 2},
            {3, 4}
        };

        
        int[][] copied = MatrixOperations.copy(original);
        copied[0][0] = 99;

        
        assertEquals(1, original[0][0]);
        assertEquals(99, copied[0][0]);
    }

    @Test
    void testCopy_handlesEmptyMatrix() {
        
        int[][] empty = new int[0][0];
        int[][] copied = MatrixOperations.copy(empty);
        
        assertEquals(0, copied.length);
    }

    
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
        
        int[][] board = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        
        
        int[][] brick = {
            {2, 2},
            {2, 0}
        };

        int[][] result = MatrixOperations.merge(board, brick, 1, 0);

        
        assertEquals(2, result[0][1]);
        assertEquals(2, result[0][2]);
        assertEquals(2, result[1][1]);
        
        assertEquals(2, result[1][1]);
    }

    @Test
    void testMerge_doesNotModifyOriginalBoard() {
        
        int[][] board = {
            {0, 0},
            {0, 0}
        };
        int[][] brick = {
            {1, 1}
        };

        MatrixOperations.merge(board, brick, 0, 0);

        
        assertEquals(0, board[0][0]);
        assertEquals(0, board[0][1]);
    }

    
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
    void testMerge_ignoresCellsOutsideBoardBounds() {
        int[][] board = new int[2][2];
        int[][] brick = {
            {7, 7},
            {7, 7}
        };

        int[][] merged = MatrixOperations.merge(board, brick, 1, 1);

        assertEquals(7, merged[1][1]);
        assertEquals(0, merged[0][1]);
        assertEquals(0, merged[1][0]);
    }

    @Test
    void testClearRows_removesFullRows() {
        
        int[][] matrix = {
            {1, 1, 1},  
            {0, 1, 0},  
            {2, 2, 2}   
        };

        RowClearResult result = MatrixOperations.clearRows(matrix);

        
        assertEquals(2, result.getLinesRemoved());
        
        
        int[][] newMatrix = result.getNewMatrix();
        assertEquals(0, newMatrix[2][0]);
        assertEquals(1, newMatrix[2][1]);
        assertEquals(0, newMatrix[2][2]);
        
        
        assertEquals(0, newMatrix[0][0]);
        assertEquals(0, newMatrix[0][1]);
    }

    @Test
    void testClearRows_handlesNoFullRows() {
        
        int[][] matrix = {
            {1, 0, 1},
            {0, 1, 0},
            {1, 0, 1}
        };

        RowClearResult result = MatrixOperations.clearRows(matrix);

        
        assertEquals(0, result.getLinesRemoved());
    }

    
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
        
        int[][] matrix1 = {{1, 2}, {3, 4}};
        int[][] matrix2 = {{5, 6}, {7, 8}};
        
        List<int[][]> original = new ArrayList<>();
        original.add(matrix1);
        original.add(matrix2);

        
        List<int[][]> copied = MatrixOperations.deepCopyList(original);

        
        copied.get(0)[0][0] = 99;

        
        assertEquals(1, original.get(0)[0][0]);
        assertEquals(99, copied.get(0)[0][0]);
    }

    @Test
    void testDeepCopyList_handlesEmptyList() {
        
        List<int[][]> empty = new ArrayList<>();
        List<int[][]> copied = MatrixOperations.deepCopyList(empty);
        
        assertTrue(copied.isEmpty());
    }
}

