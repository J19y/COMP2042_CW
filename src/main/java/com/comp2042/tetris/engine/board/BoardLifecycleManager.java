package com.comp2042.tetris.engine.board;

import java.util.Random;

/**
 * Manages board lifecycle operations including game reset.
 * <p>
 * Handles new game initialization and adding garbage lines
 * for mystery mode gameplay.
 * </p>
 *
 * @version 1.0
 */
public class BoardLifecycleManager implements BoardLifecycle {
    private final BoardState boardState;

    public BoardLifecycleManager(BoardState boardState) {
        this.boardState = boardState;
    }

    @Override
    public void newGame() {
        boardState.setBoardMatrix(new int[boardState.getRows()][boardState.getCols()]);

        new com.comp2042.tetris.engine.spawn.BrickSpawn() {
            @Override
            public com.comp2042.tetris.domain.model.SpawnResult spawnBrick() {
                com.comp2042.tetris.engine.bricks.Brick currentBrick = boardState.getBrickGenerator().getBrick();
                boardState.getBrickRotator().setBrick(currentBrick);
                int[][] shape = boardState.getBrickRotator().getCurrentShape();
                int spawnX = Math.max(0, (boardState.getCols() - shape[0].length) / 2);
                boardState.getPositionManager().reset(spawnX, 0);
                boolean gameOver = com.comp2042.tetris.util.CollisionDetector.isCollision(boardState.getBoardMatrix(), boardState.getBrickRotator().getCurrentShape(), boardState.getPositionManager().getX(), boardState.getPositionManager().getY());
                return new com.comp2042.tetris.domain.model.SpawnResult(gameOver);
            }
        }.spawnBrick();
    }

    public void addGarbageLine() {
        int[][] newMatrix = new int[boardState.getRows()][boardState.getCols()];

        for (int r = 0; r < boardState.getRows() - 1; r++) {
            System.arraycopy(boardState.getBoardMatrix()[r + 1], 0, newMatrix[r], 0, boardState.getCols());
        }

        Random rnd = new Random();
        int[] bottom = new int[boardState.getCols()];
        int holeIndex = rnd.nextInt(boardState.getCols());
        for (int c = 0; c < boardState.getCols(); c++) {
            if (c == holeIndex) {
                bottom[c] = 0;
            } else {
                bottom[c] = rnd.nextBoolean() ? 1 : 0;
            }
        }

        boolean anyBlock = false;
        for (int c = 0; c < boardState.getCols(); c++) {
            if (bottom[c] != 0) { anyBlock = true; break; }
        }
        if (!anyBlock) {
            for (int c = 0; c < boardState.getCols(); c++) {
                if (c != holeIndex) { bottom[c] = 1; break; }
            }
        }
        newMatrix[boardState.getRows() - 1] = bottom;
        boardState.setBoardMatrix(newMatrix);
    }
}
