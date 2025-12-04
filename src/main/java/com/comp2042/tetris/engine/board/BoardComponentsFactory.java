package com.comp2042.tetris.engine.board;

import com.comp2042.tetris.engine.bricks.BrickGenerator;
import com.comp2042.tetris.engine.movement.BrickPositionManager;
import com.comp2042.tetris.engine.rotation.BrickRotator;




public interface BoardComponentsFactory {
    BrickGenerator createGenerator();

    BrickRotator createRotator();

    BrickPositionManager createPositionManager();
}

