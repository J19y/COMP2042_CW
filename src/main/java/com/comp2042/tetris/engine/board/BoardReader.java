package com.comp2042.tetris.engine.board;

import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.engine.bricks.Brick;
import com.comp2042.tetris.util.CollisionDetector;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads board state for rendering purposes.
 * <p>
 * Provides read-only access to the board matrix and computes
 * view data including ghost piece position and next bricks.
 * </p>
 *
 * @version 1.0
 */
public class BoardReader implements BoardRead {
    private final BoardState boardState;

    public BoardReader(BoardState boardState) {
        this.boardState = boardState;
    }

    @Override
    public int[][] getBoardMatrix() {
        return boardState.getBoardMatrix();
    }

    @Override
    public ViewData getViewData() {
        List<Brick> nextBricks = boardState.getBrickGenerator().peekNextBricks(3);
        List<int[][]> nextBrickMatrices = new ArrayList<>();
        for (Brick b : nextBricks) {
            nextBrickMatrices.add(b.getRotationMatrix().get(0));
        }

        int ghostY = boardState.getPositionManager().getY();
        int[][] currentShape = boardState.getBrickRotator().getCurrentShape();
        int currentX = boardState.getPositionManager().getX();

        while (!CollisionDetector.isCollision(boardState.getBoardMatrix(), currentShape, currentX, ghostY + 1)) {
            ghostY++;
        }

        return new ViewData(currentShape, currentX, boardState.getPositionManager().getY(), nextBrickMatrices, ghostY);
    }
}
