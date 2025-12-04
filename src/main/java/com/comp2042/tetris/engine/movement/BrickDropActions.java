package com.comp2042.tetris.engine.movement;

import com.comp2042.tetris.domain.model.RowClearResult;


public interface BrickDropActions {
    boolean moveBrickDown();
    void mergeBrickToBackground();
    RowClearResult clearRows();
}

