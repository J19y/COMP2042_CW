package com.comp2042.tetris.mechanics.board;

import com.comp2042.tetris.domain.model.SpawnResult;
import com.comp2042.tetris.mechanics.bricks.Brick;
import com.comp2042.tetris.util.CollisionDetector;

public class BoardSpawner implements com.comp2042.tetris.mechanics.spawn.BrickSpawn {
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
