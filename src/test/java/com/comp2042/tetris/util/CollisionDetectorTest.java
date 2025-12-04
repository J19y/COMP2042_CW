package com.comp2042.tetris.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


class CollisionDetectorTest {

    @Test
    void detectsCollisionWhenBrickHitsFilledCell() {
        
        int[][] board = {
            {0, 0, 0},
            {1, 0, 0},
            {0, 0, 0}
        };
        
        int[][] brick = {
            {1}
        };

        
        assertTrue(CollisionDetector.isCollision(board, brick, 0, 1));
    }

    @Test
    void detectsCollisionWhenBrickWouldLeaveBoard() {
        
        int[][] board = new int[2][2];
        int[][] brick = {
            {1}
        };

        
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
        
        int[][] board = new int[4][4];
        int[][] brick = {
            {1, 0},
            {0, 1}
        };

        
        assertFalse(CollisionDetector.isCollision(board, brick, 1, 1));
    }

    
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

    
    @Test
    void detectsCollisionWhenSquareBrickWouldFallOffBottom() {
        int[][] board = new int[3][3];
        int[][] brick = {
            {1, 0},
            {0, 1}
        };

        assertTrue(CollisionDetector.isCollision(board, brick, 1, 2));
    }

    
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

