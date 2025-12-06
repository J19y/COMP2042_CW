package com.comp2042.tetris.engine.bricks;

/**
 * Factory for creating RandomBrickGenerator instances.
 * <p>
 * Implements BrickGeneratorFactory to provide random
 * brick generation using the 7-bag algorithm.
 * </p>
 *
 * @version 1.0
 */
public class RandomBrickGeneratorFactory implements BrickGeneratorFactory {
    @Override
    public BrickGenerator create() {
        return new RandomBrickGenerator();
    }
}

