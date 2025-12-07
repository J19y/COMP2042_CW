package com.comp2042.tetris.engine.board;

import com.comp2042.tetris.engine.bricks.BrickGenerator;
import com.comp2042.tetris.engine.movement.BrickPositionManager;
import com.comp2042.tetris.engine.rotation.BrickRotator;

/**
 * Factory interface for creating board component dependencies.
 * <p>
 * Abstracts the creation of brick generator, rotator, and position
 * manager to support dependency injection and testing.
 * </p>
 *
 * @version 1.0
 */
public interface BoardComponentsFactory {
    BrickGenerator createGenerator();

    BrickRotator createRotator();

    BrickPositionManager createPositionManager();
}

