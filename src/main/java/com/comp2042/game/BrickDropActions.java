package com.comp2042.game;

import com.comp2042.model.RowClearResult;


public interface BrickDropActions {
    boolean moveBrickDown();
    void mergeBrickToBackground();
    RowClearResult clearRows();
}
