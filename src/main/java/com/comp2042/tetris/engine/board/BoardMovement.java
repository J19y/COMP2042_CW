package com.comp2042.tetris.engine.board;

import com.comp2042.tetris.util.CollisionDetector;
import com.comp2042.tetris.util.MatrixOperations;

/**
 * Implements brick movement operations on the board.
 * <p>
 * Handles left, right, and rotation movements with collision
 * detection to ensure valid brick positions.
 * </p>
 *
 * @version 1.0
 */
public class BoardMovement implements com.comp2042.tetris.engine.movement.BrickMovement {
    private final BoardState boardState;

    public BoardMovement(BoardState boardState) {
        this.boardState = boardState;
    }

    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(boardState.getBoardMatrix());
        java.awt.Point p = boardState.getPositionManager().calculateMoveLeft();
        boolean conflict = CollisionDetector.isCollision(currentMatrix, boardState.getBrickRotator().getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            boardState.getPositionManager().updatePosition(p);
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(boardState.getBoardMatrix());
        java.awt.Point p = boardState.getPositionManager().calculateMoveRight();
        boolean conflict = CollisionDetector.isCollision(currentMatrix, boardState.getBrickRotator().getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            boardState.getPositionManager().updatePosition(p);
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(boardState.getBoardMatrix());
        com.comp2042.tetris.domain.model.RotationInfo nextShape = boardState.getBrickRotator().getNextShape();
        int[][] shape = nextShape.getShape();
        int currentX = boardState.getPositionManager().getX();
        int currentY = boardState.getPositionManager().getY();

        if (!CollisionDetector.isCollision(currentMatrix, shape, currentX, currentY)) {
            boardState.getBrickRotator().setCurrentShape(nextShape.getPosition());
            return true;
        }

        if (!CollisionDetector.isCollision(currentMatrix, shape, currentX + 1, currentY)) {
            boardState.getPositionManager().updatePosition(new java.awt.Point(currentX + 1, currentY));
            boardState.getBrickRotator().setCurrentShape(nextShape.getPosition());
            return true;
        }

        if (!CollisionDetector.isCollision(currentMatrix, shape, currentX - 1, currentY)) {
            boardState.getPositionManager().updatePosition(new java.awt.Point(currentX - 1, currentY));
            boardState.getBrickRotator().setCurrentShape(nextShape.getPosition());
            return true;
        }

        return false;
    }
}
