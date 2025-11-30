package com.comp2042.tetris.mechanics.board;

import com.comp2042.tetris.domain.model.RowClearResult;
import com.comp2042.tetris.util.MatrixOperations;

public class BoardDropActions implements com.comp2042.tetris.mechanics.movement.BrickDropActions {
    private final BoardState boardState;

    public BoardDropActions(BoardState boardState) {
        this.boardState = boardState;
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(boardState.getBoardMatrix());
        java.awt.Point p = boardState.getPositionManager().calculateMoveDown();
        boolean conflict = com.comp2042.tetris.util.CollisionDetector.isCollision(currentMatrix, boardState.getBrickRotator().getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            boardState.getPositionManager().updatePosition(p);
            return true;
        }
    }

    @Override
    public void mergeBrickToBackground() {
        boardState.setBoardMatrix(MatrixOperations.merge(boardState.getBoardMatrix(), boardState.getBrickRotator().getCurrentShape(), boardState.getPositionManager().getX(), boardState.getPositionManager().getY()));
    }

    @Override
    public RowClearResult clearRows() {
        RowClearResult result = MatrixOperations.clearRows(boardState.getBoardMatrix());
        boardState.setBoardMatrix(result.getNewMatrix());
        return result;
    }
}
