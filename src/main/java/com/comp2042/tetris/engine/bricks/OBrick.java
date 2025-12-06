package com.comp2042.tetris.engine.bricks;

import java.util.ArrayList;
import java.util.List;

import com.comp2042.tetris.util.MatrixOperations;

/**
 * O-shaped tetromino (square block).
 * Has only 1 rotation state (square is symmetric).
 * Color value is 4 (yellow).
 *
 */
final class OBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public OBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getRotationMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

}

