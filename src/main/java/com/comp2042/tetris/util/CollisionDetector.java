package com.comp2042.tetris.util;

/**
 * Utility class for detecting collisions between bricks and the game board.
 * <p>
 * Provides static methods to check if a brick at a given position
 * would collide with existing blocks or board boundaries.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public final class CollisionDetector {
    
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private CollisionDetector() {}

    /**
     * Checks if a brick at the specified position collides with the board.
     * <p>
     * A collision occurs when any non-zero cell of the brick:
     * </p>
     * <ul>
     *   <li>Falls outside the board boundaries</li>
     *   <li>Overlaps with an existing non-zero cell on the board</li>
     * </ul>
     *
     * @param matrix the game board matrix
     * @param brick the brick shape matrix to check
     * @param x the x-coordinate (column) of the brick position
     * @param y the y-coordinate (row) of the brick position
     * @return {@code true} if a collision is detected, {@code false} otherwise
     */
    public static boolean isCollision(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + j;
                int targetY = y + i;
                if (brick[i][j] != 0) {
                    if (outOfBounds(matrix, targetX, targetY) || matrix[targetY][targetX] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean outOfBounds(int[][] matrix, int targetX, int targetY) {
        if (targetY < 0 || targetY >= matrix.length) {
            return true;
        }
        return targetX < 0 || targetX >= matrix[targetY].length;
    }
}

