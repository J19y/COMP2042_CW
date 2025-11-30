package com.comp2042.tetris.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


class CollisionDetectorTest {

    @Test
    void detectsCollisionWhenBrickHitsFilledCell() {
        // Board already has a filled cell in the second row
        int[][] board = {
            {0, 0, 0},
            {1, 0, 0},
            {0, 0, 0}
        };
        // Brick will place a block on top of that cell
        int[][] brick = {
            {1}
        };

        // Placing at (0, 1) should collide with the existing block
        assertTrue(CollisionDetector.isCollision(board, brick, 0, 1));
    }

    @Test
    void detectsCollisionWhenBrickWouldLeaveBoard() {
        // Tiny 2x2 board makes it easy to test boundaries
        int[][] board = new int[2][2];
        int[][] brick = {
            {1}
        };

        // Moving left, right, or below the board should all count as collisions
        assertTrue(CollisionDetector.isCollision(board, brick, -1, 0));
        assertTrue(CollisionDetector.isCollision(board, brick, 2, 0));
        assertTrue(CollisionDetector.isCollision(board, brick, 0, 2));
    }

    @Test
    void detectsCollisionWhenBrickPositionsAboveBoard() {
        int[][] board = new int[4][4];
        int[][] brick = {
            {1, 1},
            {1, 0}
        };

        assertTrue(CollisionDetector.isCollision(board, brick, 0, -1));
    }

    @Test
    void noCollisionWhenPlacementIsClear() {
        // Empty board gives plenty of room for the brick
        int[][] board = new int[4][4];
        int[][] brick = {
            {1, 0},
            {0, 1}
        };

        // Placing in the middle should be safe
        assertFalse(CollisionDetector.isCollision(board, brick, 1, 1));
    }

    // Even offset placements should detect overlap with existing filled cells.
    @Test
    void detectsCollisionWhenBrickOverlapsOffsetFilledCell() {
        int[][] board = new int[5][5];
        board[3][3] = 9;
        int[][] brick = {
            {1, 0},
            {0, 1}
        };

        assertTrue(CollisionDetector.isCollision(board, brick, 2, 2));
    }

    // Dropping near the bottom should count as a collision once it overflows.
    @Test
    void detectsCollisionWhenSquareBrickWouldFallOffBottom() {
        int[][] board = new int[3][3];
        int[][] brick = {
            {1, 0},
            {0, 1}
        };

        assertTrue(CollisionDetector.isCollision(board, brick, 1, 2));
    }

    // A brick with only zeros should never collide regardless of placement.
    @Test
    void zeroOnlyBrickNeverCollides() {
        int[][] board = new int[2][2];
        int[][] emptyBrick = {
            {0, 0},
            {0, 0}
        };

        assertFalse(CollisionDetector.isCollision(board, emptyBrick, 0, 0));
        assertFalse(CollisionDetector.isCollision(board, emptyBrick, 1, 1));
    }
}
