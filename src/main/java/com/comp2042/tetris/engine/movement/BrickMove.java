package com.comp2042.tetris.engine.movement;

import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.engine.board.BoardRead;

/**
 * Handles horizontal movement and rotation of the active brick.
 * Coordinates between the movement component and board reader to
 * provide updated view data after each movement operation.
 *
 */
public final class BrickMove {

    private final BrickMovement movement;
    private final BoardRead reader;

    public BrickMove(BrickMovement movement, BoardRead reader) {
        this.movement = movement;
        this.reader = reader;
    }

    
    public ViewData handleLeftMove() {
        movement.moveBrickLeft();
        return reader.getViewData();
    }

    
    public ViewData handleRightMove() {
        movement.moveBrickRight();
        return reader.getViewData();
    }

    
    public ViewData handleRotation() {
        movement.rotateLeftBrick();
        return reader.getViewData();
    }
}

