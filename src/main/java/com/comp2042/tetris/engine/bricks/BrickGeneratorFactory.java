package com.comp2042.tetris.engine.bricks;

/**
 * Factory interface for creating BrickGenerator instances.
 * <p>
 * Functional interface enabling different brick generation
 * strategies to be injected into the game.
 * </p>
 *
 * @version 1.0
 */
@FunctionalInterface
public interface BrickGeneratorFactory {
    BrickGenerator create();
}

