package com.comp2042.logic.bricks;

/**
 * Interface for generating Tetris bricks.
 * Defines methods for getting current and next bricks in the game.
 */
public interface BrickGenerator {

    Brick getBrick();

    Brick getNextBrick();
}
