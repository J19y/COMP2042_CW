package com.comp2042.tetris.mechanics.piece;


// Factory interface for creating BrickGenerator implementations.

@FunctionalInterface
public interface BrickGeneratorFactory {
    BrickGenerator create();
}
