package com.comp2042.tetris.engine.movement;

/**
 * Interface for horizontal brick movement and rotation.
 * Returns true if the movement was successful, false if blocked.
 */
public interface BrickMovement {
    boolean moveBrickLeft();
    boolean moveBrickRight();
    boolean rotateLeftBrick();
}

