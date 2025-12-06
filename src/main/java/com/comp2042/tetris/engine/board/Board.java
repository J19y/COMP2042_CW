package com.comp2042.tetris.engine.board;

import com.comp2042.tetris.domain.model.RowClearResult;
import com.comp2042.tetris.domain.model.SpawnResult;
import com.comp2042.tetris.domain.model.ViewData;

/**
 * Core interface defining the game board contract.
 * Provides methods for brick movement, rotation, spawning,
 * row clearing, and board state access. Central to game logic.
 *
 */
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

