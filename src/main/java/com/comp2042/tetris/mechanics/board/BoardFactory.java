package com.comp2042.tetris.mechanics.board;


// Factory method abstraction for constructing boards with specific dimensions.

public interface BoardFactory {
    SimpleBoard create(int rows, int cols);
}
