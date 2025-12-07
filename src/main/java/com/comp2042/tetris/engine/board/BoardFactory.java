package com.comp2042.tetris.engine.board;

/**
 * Factory interface for creating game boards.
 * Allows different board implementations to be injected.
 */
public interface BoardFactory {
    SimpleBoard create(int rows, int cols);
}

