package com.comp2042.tetris.mechanics.board;

import com.comp2042.tetris.mechanics.movement.BrickPositionManager;
import com.comp2042.tetris.mechanics.piece.BrickGenerator;
import com.comp2042.tetris.mechanics.rotation.BrickRotator;


// Abstract factory that produces cohesive board collaborators.

public interface BoardComponentsFactory {
    BrickGenerator createGenerator();

    BrickRotator createRotator();

    BrickPositionManager createPositionManager();
}
