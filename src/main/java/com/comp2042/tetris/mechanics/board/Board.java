package com.comp2042.tetris.mechanics.board;

import com.comp2042.tetris.domain.model.RowClearResult;
import com.comp2042.tetris.domain.model.SpawnResult;
import com.comp2042.tetris.domain.model.ViewData;


public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    
    
    SpawnResult spawnBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    RowClearResult clearRows();

    void newGame();
}

