package com.comp2042.tetris.engine.board;

import com.comp2042.tetris.engine.movement.BrickDropActions;
import com.comp2042.tetris.engine.movement.BrickMovement;
import com.comp2042.tetris.engine.spawn.BrickSpawn;

/**
 * Aggregates all board-related interfaces into a single access point.
 * Provides movement, drop actions, reading, spawning, and lifecycle ports.
 */
public interface BoardPorts {
    BrickMovement movement();

    BrickDropActions dropActions();

    BoardRead reader();

    BrickSpawn spawner();

    BoardLifecycle lifecycle();
}

