package com.comp2042.tetris.mechanics.bricks;


// Factory interface for creating BrickGenerator implementations.

@FunctionalInterface
public interface BrickGeneratorFactory {
    BrickGenerator create();
}
