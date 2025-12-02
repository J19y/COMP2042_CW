package com.comp2042.tetris.engine.board;

import com.comp2042.tetris.domain.model.ViewData;

public interface BoardRead {
    int[][] getBoardMatrix();
    ViewData getViewData();
}

