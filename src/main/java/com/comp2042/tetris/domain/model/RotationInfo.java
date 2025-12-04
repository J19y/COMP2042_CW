package com.comp2042.tetris.domain.model;

import com.comp2042.tetris.util.MatrixOperations;


public final class RotationInfo {

    private final int[][] shape;
    private final int position;

    public RotationInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    public int getPosition() {
        return position;
    }
}

