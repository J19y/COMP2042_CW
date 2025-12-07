package com.comp2042.tetris.engine.bricks;

/**
 * Interface for brick generation strategies.
 * Provides methods to get the next brick and preview upcoming bricks.
 * Implementations can use different generation algorithms (random, bag, etc.).
 *
 */
public interface BrickGenerator {

    Brick getBrick();
    
    Brick peekNextBrick();

    
    
    java.util.List<Brick> peekNextBricks(int count);
}

