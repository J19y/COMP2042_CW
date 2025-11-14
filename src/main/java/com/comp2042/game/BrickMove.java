package com.comp2042.game;

import com.comp2042.model.ViewData;

/**
 * Handles brick movement operations (left, right, rotate).
 * Depends only on minimal movement and read-only board access.
 */
public final class BrickMove {

    private final BrickMovement movement;
    private final BoardRead reader;

    public BrickMove(BrickMovement movement, BoardRead reader) {
        this.movement = movement;
        this.reader = reader;
    }

    // Handle moving the brick left.
    public ViewData handleLeftMove() {
        movement.moveBrickLeft();
        return reader.getViewData();
    }

    // Handle moving the brick right.
    public ViewData handleRightMove() {
        movement.moveBrickRight();
        return reader.getViewData();
    }

    // Handle rotating the brick.
    public ViewData handleRotation() {
        movement.rotateLeftBrick();
        return reader.getViewData();
    }
}
