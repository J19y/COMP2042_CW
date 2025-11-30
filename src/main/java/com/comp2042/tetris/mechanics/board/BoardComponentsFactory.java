package com.comp2042.tetris.mechanics.board;

import com.comp2042.tetris.mechanics.bricks.BrickGenerator;
import com.comp2042.tetris.mechanics.movement.BrickPositionManager;
import com.comp2042.tetris.mechanics.rotation.BrickRotator;




public interface BoardComponentsFactory {
    BrickGenerator createGenerator();

    BrickRotator createRotator();

    BrickPositionManager createPositionManager();
}

