package com.comp2042.tetris.domain.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class RowClearResultTest {

    @Test
    void constructorWithValidData() {
        int[][] matrix = {{1, 1}, {0, 0}};
        List<Integer> clearedRows = new ArrayList<>();
        clearedRows.add(0);

        RowClearResult result = new RowClearResult(1, matrix, 50, clearedRows);

        assertEquals(1, result.getLinesRemoved());
        assertEquals(50, result.getScoreBonus());
        assertEquals(1, result.getClearedRows().size());
    }

    @Test
    void getNewMatrixReturnsIndependentCopy() {
        int[][] original = {{1, 2}, {3, 4}};
        RowClearResult result = new RowClearResult(1, original, 50, null);

        int[][] retrieved = result.getNewMatrix();
        retrieved[0][0] = 99;

        int[][] retrievedAgain = result.getNewMatrix();
        assertEquals(1, retrievedAgain[0][0], "Original matrix modification should not affect stored value");
    }

    @Test
    void getNewMatrixMultipleCallsReturnIndependentCopies() {
        int[][] matrix = {{5, 6}, {7, 8}};
        RowClearResult result = new RowClearResult(2, matrix, 200, null);

        int[][] first = result.getNewMatrix();
        int[][] second = result.getNewMatrix();

        assertNotSame(first, second, "Each call should return a new instance");
        assertArrayEquals(first, second);
    }

    @Test
    void clearedRowsListIsImmutable() {
        List<Integer> originalList = new ArrayList<>();
        originalList.add(5);
        originalList.add(10);

        RowClearResult result = new RowClearResult(2, new int[][]{{0}}, 200, originalList);

        assertThrows(UnsupportedOperationException.class, () -> 
            result.getClearedRows().add(15)
        );
    }

    @Test
    void clearedRowsListCannotBeModifiedViaAdd() {
        List<Integer> clearedRows = new ArrayList<>();
        clearedRows.add(0);
        RowClearResult result = new RowClearResult(1, new int[][]{{0}}, 50, clearedRows);

        assertThrows(UnsupportedOperationException.class, () -> 
            result.getClearedRows().add(1)
        );
    }

    @Test
    void clearedRowsListCannotBeModifiedViaRemove() {
        List<Integer> clearedRows = new ArrayList<>();
        clearedRows.add(0);
        RowClearResult result = new RowClearResult(1, new int[][]{{0}}, 50, clearedRows);

        assertThrows(UnsupportedOperationException.class, () -> 
            result.getClearedRows().remove(0)
        );
    }

    @Test
    void clearedRowsListCannotBeClearedViaModification() {
        List<Integer> clearedRows = new ArrayList<>();
        clearedRows.add(5);
        RowClearResult result = new RowClearResult(1, new int[][]{{0}}, 50, clearedRows);

        assertThrows(UnsupportedOperationException.class, () -> 
            result.getClearedRows().clear()
        );
    }

    @Test
    void nullClearedRowsListBecomesEmpty() {
        RowClearResult result = new RowClearResult(0, new int[][]{{0}}, 0, null);

        assertTrue(result.getClearedRows().isEmpty());
        assertEquals(0, result.getClearedRows().size());
    }

    @Test
    void multipleLinesRemovedAndScoreBonusTracked() {
        int[][] matrix = new int[20][10];
        List<Integer> clearedRows = new ArrayList<>();
        clearedRows.add(5);
        clearedRows.add(10);
        clearedRows.add(15);

        RowClearResult result = new RowClearResult(3, matrix, 1200, clearedRows);

        assertEquals(3, result.getLinesRemoved());
        assertEquals(1200, result.getScoreBonus());
        assertEquals(3, result.getClearedRows().size());
        assertEquals(5, result.getClearedRows().get(0).intValue());
        assertEquals(10, result.getClearedRows().get(1).intValue());
        assertEquals(15, result.getClearedRows().get(2).intValue());
    }

    @Test
    void zeroLinesRemovedAndZeroScoreBonus() {
        RowClearResult result = new RowClearResult(0, new int[][]{{1, 1}}, 0, null);

        assertEquals(0, result.getLinesRemoved());
        assertEquals(0, result.getScoreBonus());
        assertTrue(result.getClearedRows().isEmpty());
    }

    @Test
    void largeMatrixIndependencePreserved() {
        int[][] large = new int[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                large[i][j] = (i + j) % 2;
            }
        }

        RowClearResult result = new RowClearResult(1, large, 50, null);
        int[][] retrieved = result.getNewMatrix();
        retrieved[50][50] = 99;

        int[][] retrievedAgain = result.getNewMatrix();
        assertEquals(0, retrievedAgain[50][50], "Large matrix modification isolation");
    }

    @Test
    void emptyMatrixHandled() {
        int[][] empty = new int[0][0];
        RowClearResult result = new RowClearResult(0, empty, 0, null);

        int[][] retrieved = result.getNewMatrix();
        assertEquals(0, retrieved.length);
    }

    @Test
    void jaggedMatrixHandledCorrectly() {
        int[][] jagged = {{1}, {2, 3, 4}, {5, 6}};
        RowClearResult result = new RowClearResult(1, jagged, 50, null);

        int[][] retrieved = result.getNewMatrix();
        assertEquals(1, retrieved[0].length);
        assertEquals(3, retrieved[1].length);
        assertEquals(2, retrieved[2].length);

        retrieved[1][2] = 99;
        int[][] retrievedAgain = result.getNewMatrix();
        assertEquals(4, retrievedAgain[1][2]);
    }

    @Test
    void negativeScoreBonusAllowed() {
        RowClearResult result = new RowClearResult(1, new int[][]{{1}}, -100, null);
        assertEquals(-100, result.getScoreBonus());
    }

    @Test
    void largeLinesRemovedValue() {
        RowClearResult result = new RowClearResult(Integer.MAX_VALUE, new int[][]{{1}}, 999999, null);
        assertEquals(Integer.MAX_VALUE, result.getLinesRemoved());
        assertEquals(999999, result.getScoreBonus());
    }
}
