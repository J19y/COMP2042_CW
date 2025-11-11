package com.comp2042.util;

/**
 * Created collisionDetector class to just handle collision detection logic.
 * Removed collision detection logic from Matrix operation to support the SRP.
 */
public final class CollisionDetector {
    private CollisionDetector() {}

    public static boolean isCollision(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0 && (outOfBounds(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean outOfBounds(int[][] matrix, int targetX, int targetY) {
        return !(targetX >= 0 && targetY < matrix.length && targetX < matrix[targetY].length);
    }
}
