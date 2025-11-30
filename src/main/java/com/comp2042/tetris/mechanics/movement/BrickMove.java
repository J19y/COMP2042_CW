package com.comp2042.tetris.mechanics.movement;

import com.comp2042.tetris.mechanics.board.BoardRead;

import com.comp2042.tetris.domain.model.ViewData;


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

