package com.comp2042.tetris.mechanics.board;

import com.comp2042.tetris.domain.model.ViewData;

public interface BoardRead {
    int[][] getBoardMatrix();
    ViewData getViewData();
}
