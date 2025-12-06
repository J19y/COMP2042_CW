package com.comp2042.tetris.domain.model;

import com.comp2042.tetris.util.MatrixOperations;

/**
 * Holds the next rotation matrix and rotation index for rotation operations.
 * <p>
 * Used to encapsulate the result of calculating the next rotation state
 * without immediately applying it, enabling wall kick validation.
 * </p>
 *
 * @version 1.0
 */
public final class RotationInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Constructs a RotationInfo with shape and rotation position.
     *
     * @param shape the rotated brick shape matrix
     * @param position the rotation index (0-3 for most bricks)
     */
    public RotationInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Returns a defensive copy of the rotated shape.
     *
     * @return a copy of the shape matrix
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Gets the rotation position index.
     *
     * @return the rotation index
     */
    public int getPosition() {
        return position;
    }
}

