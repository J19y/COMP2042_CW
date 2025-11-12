package com.comp2042.game;

import com.comp2042.model.ViewData;

/**
 * Handles brick movement operations (left, right, rotate).
 * Extracted from GameController to follow Single Responsibility Principle.
 */
public final class BrickMove {
    
    private final Board board;

    public BrickMove(Board board) {
        this.board = board;
    }

     // Handle moving the brick left.
    public ViewData handleLeftMove() {
        board.moveBrickLeft();
        return board.getViewData();
    }

    // Handle moving the brick right.

    public ViewData handleRightMove() {
        board.moveBrickRight();
        return board.getViewData();
    }

    // Handle rotating the brick.

    public ViewData handleRotation() {
        board.rotateLeftBrick();
        return board.getViewData();
    }
}
