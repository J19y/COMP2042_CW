package com.comp2042.tetris.mechanics.piece;

/**
 * Interface for generating Tetris bricks.
 * Defines methods for getting current and next bricks in the game.
 */
public interface BrickGenerator {

    Brick getBrick();
    /**
     * Previously named getNextBrick(). Renamed to peekNextBrick() to make the
     * intent clearer: this method peeks at the next brick in the queue without
     * removing it from the generator's buffer.
     */
    Brick peekNextBrick();
}
