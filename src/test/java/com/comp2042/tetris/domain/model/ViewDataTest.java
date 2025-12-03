package com.comp2042.tetris.domain.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ViewDataTest {

    @Test
    void constructorWithMatrixAndSingleNextBrick() {
        int[][] brickData = {{1, 0}, {0, 1}};
        int[][] nextBrick = {{2, 2}};
        ViewData view = new ViewData(brickData, 3, 5, nextBrick);

        assertEquals(3, view.getxPosition());
        assertEquals(5, view.getyPosition());
        assertEquals(5, view.getGhostY());
    }

    @Test
    void constructorWithMatrixAndListOfNextBricks() {
        int[][] brickData = {{1}};
        List<int[][]> nextBricks = new ArrayList<>();
        nextBricks.add(new int[][]{{2}});
        nextBricks.add(new int[][]{{3}});

        ViewData view = new ViewData(brickData, 1, 2, nextBricks);

        assertEquals(1, view.getxPosition());
        assertEquals(2, view.getyPosition());
        assertEquals(2, nextBricks.size());
    }

    @Test
    void constructorWithGhostYPosition() {
        int[][] brickData = {{1}};
        List<int[][]> nextBricks = new ArrayList<>();
        ViewData view = new ViewData(brickData, 2, 3, nextBricks, 10);

        assertEquals(2, view.getxPosition());
        assertEquals(3, view.getyPosition());
        assertEquals(10, view.getGhostY());
    }

    @Test
    void getBrickDataReturnsIndependentCopy() {
        int[][] original = {{1, 2}, {3, 4}};
        ViewData view = new ViewData(original, 0, 0, new int[][]{{0}});

        int[][] retrieved = view.getBrickData();
        retrieved[0][0] = 99;

        int[][] retrievedAgain = view.getBrickData();
        assertEquals(1, retrievedAgain[0][0], "Brick data should not be affected by external modification");
    }

    @Test
    void getBrickDataMultipleCallsReturnIndependentCopies() {
        int[][] brickData = {{5, 6}, {7, 8}};
        ViewData view = new ViewData(brickData, 0, 0, new int[][]{{0}});

        int[][] first = view.getBrickData();
        int[][] second = view.getBrickData();

        assertNotSame(first, second, "Each call should return independent instance");
        assertArrayEquals(first, second);
    }

    @Test
    void getNextBrickDataReturnsIndependentCopies() {
        int[][] next1 = {{1, 1}, {1, 0}};
        int[][] next2 = {{2, 2}, {0, 2}};
        List<int[][]> nextBricks = new ArrayList<>();
        nextBricks.add(next1);
        nextBricks.add(next2);

        ViewData view = new ViewData(new int[][]{{0}}, 0, 0, nextBricks);

        List<int[][]> retrieved = view.getNextBrickData();
        retrieved.get(0)[0][0] = 99;

        List<int[][]> retrievedAgain = view.getNextBrickData();
        assertEquals(1, retrievedAgain.get(0)[0][0], "Next brick data should not be affected by modification");
    }

    @Test
    void nextBrickListIsIndependentFromOriginal() {
        List<int[][]> original = new ArrayList<>();
        original.add(new int[][]{{1}});
        original.add(new int[][]{{2}});

        ViewData view = new ViewData(new int[][]{{0}}, 0, 0, original);

        List<int[][]> retrieved = view.getNextBrickData();
        retrieved.add(new int[][]{{3}});

        List<int[][]> retrievedAgain = view.getNextBrickData();
        assertEquals(2, retrievedAgain.size(), "List modification should not affect stored state");
    }

    @Test
    void positionCoordinatesPreserved() {
        ViewData view = new ViewData(new int[][]{{1}}, 7, 14, new int[][]{{0}});

        assertEquals(7, view.getxPosition());
        assertEquals(14, view.getyPosition());
    }

    @Test
    void zeroCoordinatesHandled() {
        ViewData view = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});

        assertEquals(0, view.getxPosition());
        assertEquals(0, view.getyPosition());
    }

    @Test
    void negativeCoordinatesHandled() {
        ViewData view = new ViewData(new int[][]{{1}}, -5, -3, new int[][]{{0}});

        assertEquals(-5, view.getxPosition());
        assertEquals(-3, view.getyPosition());
    }

    @Test
    void largeCoordinatesHandled() {
        ViewData view = new ViewData(new int[][]{{1}}, 1000000, 2000000, new int[][]{{0}});

        assertEquals(1000000, view.getxPosition());
        assertEquals(2000000, view.getyPosition());
    }

    @Test
    void ghostYDifferentFromYPosition() {
        List<int[][]> nextBricks = new ArrayList<>();
        nextBricks.add(new int[][]{{0}});
        ViewData view = new ViewData(new int[][]{{1}}, 5, 10, nextBricks, 15);

        assertEquals(10, view.getyPosition());
        assertEquals(15, view.getGhostY());
    }

    @Test
    void ghostYDefaultsToYPosition() {
        ViewData view = new ViewData(new int[][]{{1}}, 3, 7, new int[][]{{0}});

        assertEquals(7, view.getyPosition());
        assertEquals(7, view.getGhostY(), "Default ghost Y should equal Y position");
    }

    @Test
    void emptyNextBrickListHandled() {
        List<int[][]> empty = new ArrayList<>();
        ViewData view = new ViewData(new int[][]{{1}}, 0, 0, empty);

        List<int[][]> retrieved = view.getNextBrickData();
        assertTrue(retrieved.isEmpty());
    }

    @Test
    void largeNextBrickListHandled() {
        List<int[][]> large = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            large.add(new int[][]{{i}});
        }
        ViewData view = new ViewData(new int[][]{{0}}, 0, 0, large);

        List<int[][]> retrieved = view.getNextBrickData();
        assertEquals(100, retrieved.size());

        // Verify independence
        retrieved.clear();
        assertEquals(100, view.getNextBrickData().size(), "Original list should not be modified");
    }

    @Test
    void largeMatrixHandled() {
        int[][] large = new int[50][50];
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                large[i][j] = (i + j) % 3;
            }
        }
        ViewData view = new ViewData(large, 0, 0, new int[][]{{0}});

        int[][] retrieved = view.getBrickData();
        retrieved[25][25] = 99;

        int[][] retrievedAgain = view.getBrickData();
        assertEquals(2, retrievedAgain[25][25], "Large matrix should maintain independence");
    }

    @Test
    void emptyBrickMatrixHandled() {
        int[][] empty = new int[0][0];
        ViewData view = new ViewData(empty, 0, 0, new int[][]{{0}});

        int[][] retrieved = view.getBrickData();
        assertEquals(0, retrieved.length);
    }

    @Test
    void jaggedMatrixHandled() {
        int[][] jagged = {{1}, {2, 3}, {4, 5, 6}};
        ViewData view = new ViewData(jagged, 0, 0, new int[][]{{0}});

        int[][] retrieved = view.getBrickData();
        assertEquals(1, retrieved[0].length);
        assertEquals(2, retrieved[1].length);
        assertEquals(3, retrieved[2].length);
    }

    @Test
    void multipleNextBricksAllIndependent() {
        int[][] brick1 = {{1, 2}, {3, 4}};
        int[][] brick2 = {{5, 6}, {7, 8}};
        int[][] brick3 = {{9, 10}, {11, 12}};
        List<int[][]> nextBricks = new ArrayList<>();
        nextBricks.add(brick1);
        nextBricks.add(brick2);
        nextBricks.add(brick3);

        ViewData view = new ViewData(new int[][]{{0}}, 0, 0, nextBricks);

        List<int[][]> retrieved1 = view.getNextBrickData();
        retrieved1.get(0)[0][0] = 99;

        List<int[][]> retrieved2 = view.getNextBrickData();
        assertEquals(1, retrieved2.get(0)[0][0], "Each next brick should be independent");
    }

    @Test
    void nextBrickDataConversionFromSingleMatrix() {
        int[][] nextMatrix = {{1, 2}, {3, 4}};
        List<int[][]> nextBrickList = new ArrayList<>();
        nextBrickList.add(nextMatrix);
        ViewData view = new ViewData(new int[][]{{0}}, 1, 2, nextBrickList);

        List<int[][]> nextBricks = view.getNextBrickData();
        assertEquals(1, nextBricks.size());
        assertArrayEquals(new int[][]{{1, 2}, {3, 4}}, nextBricks.get(0));
    }
}
