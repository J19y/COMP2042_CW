package com.comp2042.tetris.engine.bricks;

import java.util.ArrayList;
import java.util.List;

import com.comp2042.tetris.util.MatrixOperations;

/**
 * Z-shaped tetromino (reverse S-shape).
 * Has 2 rotation states. Color value is 7 (red).
 *
 */
final class ZBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public ZBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {7, 7, 0, 0},
                {0, 7, 7, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 7, 0, 0},
                {7, 7, 0, 0},
                {7, 0, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getRotationMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}

