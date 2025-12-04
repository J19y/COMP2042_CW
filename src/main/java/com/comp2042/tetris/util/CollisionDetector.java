package com.comp2042.tetris.util;


public final class CollisionDetector {
    private CollisionDetector() {}

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

