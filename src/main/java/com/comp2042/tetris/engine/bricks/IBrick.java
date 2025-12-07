package com.comp2042.tetris.engine.bricks;

import java.util.ArrayList;
import java.util.List;

import com.comp2042.tetris.util.MatrixOperations;

/**
 * I-shaped tetromino (the long straight piece).
 * Consists of 4 cells in a row. Has 2 rotation states:
 * horizontal and vertical. Color value is 1 (cyan).
 *
 */
final class IBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public IBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        });
    }

    @Override
    public List<int[][]> getRotationMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

}

