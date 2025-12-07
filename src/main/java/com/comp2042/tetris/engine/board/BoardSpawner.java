package com.comp2042.tetris.engine.board;

import com.comp2042.tetris.domain.model.SpawnResult;
import com.comp2042.tetris.engine.bricks.Brick;
import com.comp2042.tetris.util.CollisionDetector;

/**
 * Handles brick spawning on the game board.
 * <p>
 * Creates new bricks at the spawn position and checks for
 * collision to detect game over condition.
 * </p>
 *
 * @version 1.0
 */
public class BoardSpawner implements com.comp2042.tetris.engine.spawn.BrickSpawn {
    private final BoardState boardState;

    public BoardSpawner(BoardState boardState) {
        this.boardState = boardState;
    }

    @Override
    public SpawnResult spawnBrick() {
        Brick currentBrick = boardState.getBrickGenerator().getBrick();
        boardState.getBrickRotator().setBrick(currentBrick);
        int[][] shape = boardState.getBrickRotator().getCurrentShape();
        int spawnX = Math.max(0, (boardState.getCols() - shape[0].length) / 2);
        boardState.getPositionManager().reset(spawnX, 0);
        boolean gameOver = CollisionDetector.isCollision(boardState.getBoardMatrix(), boardState.getBrickRotator().getCurrentShape(), boardState.getPositionManager().getX(), boardState.getPositionManager().getY());
        return new SpawnResult(gameOver);
    }
}
