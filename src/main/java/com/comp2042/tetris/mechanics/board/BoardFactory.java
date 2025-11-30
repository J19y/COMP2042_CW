package com.comp2042.tetris.mechanics.board;




public interface BoardFactory {
    SimpleBoard create(int rows, int cols);
}

