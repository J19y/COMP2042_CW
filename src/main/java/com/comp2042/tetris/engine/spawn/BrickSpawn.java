package com.comp2042.tetris.engine.spawn;

import com.comp2042.tetris.domain.model.SpawnResult;

/**
 * Interface for brick spawning operations.
 * Defines the contract for spawning new bricks onto the game board.
 * Returns SpawnResult indicating success or game over condition.
 *
 */
public interface BrickSpawn {
    SpawnResult spawnBrick();
}

