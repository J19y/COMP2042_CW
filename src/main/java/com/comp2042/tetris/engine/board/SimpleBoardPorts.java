package com.comp2042.tetris.engine.board;

import java.util.Objects;

import com.comp2042.tetris.engine.movement.BrickDropActions;
import com.comp2042.tetris.engine.movement.BrickMovement;
import com.comp2042.tetris.engine.spawn.BrickSpawn;

/**
 * Adapter providing BoardPorts interface for SimpleBoard.
 * Exposes board capabilities through segregated interfaces.
 */
public final class SimpleBoardPorts implements BoardPorts {

    private final SimpleBoard board;

    public SimpleBoardPorts(SimpleBoard board) {
        this.board = Objects.requireNonNull(board, "board must not be null");
    }

    @Override
    public BrickMovement movement() {
        return board;
    }

    @Override
    public BrickDropActions dropActions() {
        return board;
    }

    @Override
    public BoardRead reader() {
        return board;
    }

    @Override
    public BrickSpawn spawner() {
        return board;
    }

    @Override
    public BoardLifecycle lifecycle() {
        return board;
    }
}

