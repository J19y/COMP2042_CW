package com.comp2042.tetris.application.port;

/**
 * Single-responsibility interface for triggering a new game.
 * <p>
 * Extracted for Single Responsibility Principle (SRP) compliance,
 * allowing components to depend only on game creation capability.
 * </p>
 *
 * @version 1.0
 */
public interface CreateNewGame {
    
    /**
     * Initiates a new game, resetting the board and score.
     */
    void createNewGame();
}

