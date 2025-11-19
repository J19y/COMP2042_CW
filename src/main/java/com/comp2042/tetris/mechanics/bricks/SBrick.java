package com.comp2042.tetris.mechanics.bricks;

import java.util.ArrayList;
import java.util.List;

import com.comp2042.tetris.util.MatrixOperations;

final class SBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public SBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 5, 5, 0},
                {5, 5, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {5, 0, 0, 0},
                {5, 5, 0, 0},
                {0, 5, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getRotationMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
