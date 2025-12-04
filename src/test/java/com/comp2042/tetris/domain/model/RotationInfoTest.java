package com.comp2042.tetris.domain.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;

class RotationInfoTest {

    @Test
    void constructorStoresShapeAndPosition() {
        int[][] shape = {{1, 0}, {0, 1}};
        RotationInfo info = new RotationInfo(shape, 0);

        assertEquals(0, info.getPosition());
    }

    @Test
    void getShapeReturnsIndependentCopy() {
        int[][] original = {{1, 2}, {3, 4}};
        RotationInfo info = new RotationInfo(original, 1);

        int[][] retrieved = info.getShape();
        retrieved[0][0] = 99;

        int[][] retrievedAgain = info.getShape();
        assertEquals(1, retrievedAgain[0][0], "Shape should not be affected by external modification");
    }

    @Test
    void getShapeMultipleCallsReturnIndependentCopies() {
        int[][] shape = {{5, 6}, {7, 8}};
        RotationInfo info = new RotationInfo(shape, 2);

        int[][] first = info.getShape();
        int[][] second = info.getShape();

        assertNotSame(first, second, "Each call should return independent instance");
        assertArrayEquals(first, second);
    }

    @Test
    void positionZero() {
        RotationInfo info = new RotationInfo(new int[][]{{1}}, 0);
        assertEquals(0, info.getPosition());
    }

    @Test
    void positionOne() {
        RotationInfo info = new RotationInfo(new int[][]{{1}}, 1);
        assertEquals(1, info.getPosition());
    }

    @Test
    void positionThree() {
        RotationInfo info = new RotationInfo(new int[][]{{1}}, 3);
        assertEquals(3, info.getPosition());
    }

    @Test
    void largePosition() {
        RotationInfo info = new RotationInfo(new int[][]{{1}}, 999);
        assertEquals(999, info.getPosition());
    }

    @Test
    void negativePosition() {
        RotationInfo info = new RotationInfo(new int[][]{{1}}, -1);
        assertEquals(-1, info.getPosition());
    }

    @Test
    void largeShape() {
        int[][] large = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                large[i][j] = (i + j) % 2;
            }
        }
        RotationInfo info = new RotationInfo(large, 2);

        int[][] retrieved = info.getShape();
        retrieved[5][5] = 99;

        int[][] retrievedAgain = info.getShape();
        assertEquals(0, retrievedAgain[5][5], "Large shape modification isolation");
    }

    @Test
    void singleCellShape() {
        int[][] single = {{1}};
        RotationInfo info = new RotationInfo(single, 0);

        int[][] retrieved = info.getShape();
        assertEquals(1, retrieved.length);
        assertEquals(1, retrieved[0].length);
        assertEquals(1, retrieved[0][0]);
    }

    @Test
    void jaggedShape() {
        int[][] jagged = {{1}, {2, 3}, {4, 5, 6}};
        RotationInfo info = new RotationInfo(jagged, 1);

        int[][] retrieved = info.getShape();
        assertEquals(1, retrieved[0].length);
        assertEquals(2, retrieved[1].length);
        assertEquals(3, retrieved[2].length);

        retrieved[2][2] = 99;
        int[][] retrievedAgain = info.getShape();
        assertEquals(6, retrievedAgain[2][2]);
    }

    @Test
    void emptyShape() {
        int[][] empty = new int[0][0];
        RotationInfo info = new RotationInfo(empty, 0);

        int[][] retrieved = info.getShape();
        assertEquals(0, retrieved.length);
    }

    @Test
    void tetrisPieceOShape() {
        int[][] oShape = {
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0}
        };
        RotationInfo info = new RotationInfo(oShape, 0);

        int[][] retrieved = info.getShape();
        assertEquals(4, retrieved.length);
        assertEquals(4, retrieved[0].length);
        assertEquals(1, retrieved[1][1]);
        assertEquals(1, retrieved[2][2]);
    }

    @Test
    void tetrisPieceIShape() {
        int[][] iShape = {
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };
        RotationInfo info = new RotationInfo(iShape, 0);

        int[][] retrieved = info.getShape();
        assertEquals(1, retrieved[1][0]);
    }

    @Test
    void multipleRotationSequence() {
        int[][] shape1 = {{1}};
        int[][] shape2 = {{2}};
        int[][] shape3 = {{3}};

        RotationInfo info1 = new RotationInfo(shape1, 0);
        RotationInfo info2 = new RotationInfo(shape2, 1);
        RotationInfo info3 = new RotationInfo(shape3, 2);

        assertEquals(0, info1.getPosition());
        assertEquals(1, info2.getPosition());
        assertEquals(2, info3.getPosition());

        assertArrayEquals(new int[][]{{1}}, info1.getShape());
        assertArrayEquals(new int[][]{{2}}, info2.getShape());
        assertArrayEquals(new int[][]{{3}}, info3.getShape());
    }

    @Test
    void shapeWithAllZeros() {
        int[][] allZeros = {{0, 0}, {0, 0}};
        RotationInfo info = new RotationInfo(allZeros, 1);

        int[][] retrieved = info.getShape();
        assertEquals(0, retrieved[0][0]);
        assertEquals(0, retrieved[1][1]);
    }

    @Test
    void shapeWithMixedValues() {
        int[][] mixed = {{1, 0, 2}, {3, 0, 4}, {0, 5, 0}};
        RotationInfo info = new RotationInfo(mixed, 2);

        int[][] retrieved = info.getShape();
        assertEquals(1, retrieved[0][0]);
        assertEquals(0, retrieved[0][1]);
        assertEquals(2, retrieved[0][2]);
        assertEquals(5, retrieved[2][1]);
    }

    @Test
    void largePositionValue() {
        RotationInfo info = new RotationInfo(new int[][]{{1}}, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, info.getPosition());
    }

    @Test
    void shapeIndependenceAfterMultipleRetrievals() {
        int[][] original = {{1, 2}, {3, 4}};
        RotationInfo info = new RotationInfo(original, 0);

        int[][] first = info.getShape();
        first[0][0] = 99;

        int[][] second = info.getShape();
        second[1][1] = 88;

        int[][] third = info.getShape();
        assertEquals(1, third[0][0], "All retrieval independence");
        assertEquals(4, third[1][1], "All retrieval independence");
    }
}
