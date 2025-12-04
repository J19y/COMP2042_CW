package com.comp2042.tetris.engine.board;

import com.comp2042.tetris.engine.movement.BrickDropActions;
import com.comp2042.tetris.engine.movement.BrickMovement;
import com.comp2042.tetris.engine.spawn.BrickSpawn;


public interface BoardPorts {
    BrickMovement movement();

    BrickDropActions dropActions();

    BoardRead reader();

    BrickSpawn spawner();

    BoardLifecycle lifecycle();
}

