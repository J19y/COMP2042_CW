package com.comp2042.tetris.engine.board;

import com.comp2042.tetris.domain.model.ViewData;

/**
 * Read-only interface for accessing board state.
 * Provides the board matrix and active brick view data.
 */
public interface BoardRead {
    int[][] getBoardMatrix();
    ViewData getViewData();
}

