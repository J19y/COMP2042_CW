package com.comp2042.tetris.engine.board;




public interface BoardFactory {
    SimpleBoard create(int rows, int cols);
}

