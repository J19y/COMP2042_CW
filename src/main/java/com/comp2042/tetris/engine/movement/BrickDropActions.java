package com.comp2042.tetris.engine.movement;

import com.comp2042.tetris.domain.model.RowClearResult;

/**
 * Interface for brick drop operations and row clearing.
 * Handles downward movement, merging to background, and line clears.
 */
public interface BrickDropActions {
    boolean moveBrickDown();
    void mergeBrickToBackground();
    RowClearResult clearRows();
}

