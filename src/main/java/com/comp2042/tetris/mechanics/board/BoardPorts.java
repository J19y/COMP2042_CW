package com.comp2042.tetris.mechanics.board;

import com.comp2042.tetris.mechanics.movement.BrickDropActions;
import com.comp2042.tetris.mechanics.movement.BrickMovement;
import com.comp2042.tetris.mechanics.spawn.BrickSpawn;

// used to group all board-facing roles for injection
public interface BoardPorts {
    BrickMovement movement();

    BrickDropActions dropActions();

    BoardRead reader();

    BrickSpawn spawner();

    BoardLifecycle lifecycle();
}
