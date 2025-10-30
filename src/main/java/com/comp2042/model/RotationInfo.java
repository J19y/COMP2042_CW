package com.comp2042.model;

import com.comp2042.util.MatrixOperations;

/**
 * Renamed from NextShapeInfo to RotationInfo to show that this holds a single
 * rotation matrix for the next shape and its rotation index. Kept the getter
 * names (getShape/getPosition)
 */
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
