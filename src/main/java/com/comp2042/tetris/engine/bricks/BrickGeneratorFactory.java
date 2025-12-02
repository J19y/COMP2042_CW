package com.comp2042.tetris.engine.bricks;




@FunctionalInterface
public interface BrickGeneratorFactory {
    BrickGenerator create();
}

