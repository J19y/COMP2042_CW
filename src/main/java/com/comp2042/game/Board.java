package com.comp2042.game;

import com.comp2042.model.RowClearResult;
import com.comp2042.model.Score;
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

    /**
     * Previously named createNewBrick(). Renamed to spawnBrick() to clarify
     * that the method spawns a new active brick on the board not just create it.
     */
    boolean spawnBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    RowClearResult clearRows();

    Score getScore();

    void newGame();
}
