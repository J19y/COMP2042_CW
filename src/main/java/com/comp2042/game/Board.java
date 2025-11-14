package com.comp2042.game;

import com.comp2042.model.RowClearResult;
import com.comp2042.model.SpawnResult;
import com.comp2042.model.ViewData;

/**
 * Interface defining the core game board operations.
 * This interface encapsulates all the essential game mechanics like brick movement,
 * rotation, and board state management.
 */
public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    
    // Spawns a new active brick at its initial position.
    SpawnResult spawnBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    RowClearResult clearRows();

    void newGame();
}
