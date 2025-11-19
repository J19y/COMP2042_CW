package com.comp2042.tetris.mechanics.rotation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.comp2042.tetris.domain.model.RotationInfo;
import com.comp2042.tetris.mechanics.bricks.Brick;

class BrickRotatorTest {

    @Test
    void setBrickInitializesCurrentShape() {
        BrickRotator rotator = new BrickRotator();
        Brick brick = brickWithShapes(
                new int[][]{{1, 0}, {0, 1}},
                new int[][]{{0, 1}, {1, 0}});

        rotator.setBrick(brick);

        // After assigning a brick we should start at the first rotation
        assertEquals(0, rotator.getCurrentShapeIndex());
        assertMatrixEquals(new int[][]{{1, 0}, {0, 1}}, rotator.getCurrentShape());
    }

    @Test
    void getNextShapeWrapsAroundAndKeepsCurrentIndex() {
        BrickRotator rotator = new BrickRotator();
        Brick brick = brickWithShapes(
                new int[][]{{1, 1}, {0, 0}},
                new int[][]{{0, 0}, {1, 1}});

        rotator.setBrick(brick);
        rotator.setCurrentShape(1);

        RotationInfo next = rotator.getNextShape();

        // When we are at the last rotation, asking for the next should wrap to index 0
        assertEquals(0, next.getPosition());
        assertMatrixEquals(new int[][]{{1, 1}, {0, 0}}, next.getShape());
        // Calling getNextShape should not mutate the current index internally
        assertEquals(1, rotator.getCurrentShapeIndex());
    }

    @Test
    void setBrickRejectsNullOrEmptyBricks() {
        BrickRotator rotator = new BrickRotator();

        // Null bricks should be rejected up front
        assertThrows(IllegalArgumentException.class, () -> rotator.setBrick(null));

        // Bricks without rotations are also invalid
        Brick emptyBrick = () -> Collections.emptyList();
        assertThrows(IllegalArgumentException.class, () -> rotator.setBrick(emptyBrick));
    }

    private static Brick brickWithShapes(int[][]... shapes) {
        List<int[][]> matrices = Arrays.asList(shapes);
        return () -> matrices;
    }

    private static void assertMatrixEquals(int[][] expected, int[][] actual) {
        assertEquals(expected.length, actual.length);
        for (int row = 0; row < expected.length; row++) {
            assertArrayEquals(expected[row], actual[row]);
        }
    }
}
