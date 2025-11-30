package com.comp2042.tetris.mechanics.board;

import com.comp2042.tetris.mechanics.bricks.BrickGenerator;
import com.comp2042.tetris.mechanics.movement.BrickPositionManager;
import com.comp2042.tetris.mechanics.rotation.BrickRotator;

public class BoardState {
    private final int rows;
    private final int cols;
    private int[][] boardMatrix;
    private final BrickPositionManager positionManager;
    private final BrickRotator brickRotator;
    private final BrickGenerator brickGenerator;

    public BoardState(int rows, int cols, BrickPositionManager positionManager,
                      BrickRotator brickRotator, BrickGenerator brickGenerator) {
        this.rows = rows;
        this.cols = cols;
        this.boardMatrix = new int[rows][cols];
        this.positionManager = positionManager;
        this.brickRotator = brickRotator;
        this.brickGenerator = brickGenerator;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    public void setBoardMatrix(int[][] boardMatrix) {
        this.boardMatrix = boardMatrix;
    }

    public BrickPositionManager getPositionManager() {
        return positionManager;
    }

    public BrickRotator getBrickRotator() {
        return brickRotator;
    }

    public BrickGenerator getBrickGenerator() {
        return brickGenerator;
    }
}
