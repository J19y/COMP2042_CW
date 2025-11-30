package com.comp2042.tetris.mechanics.board;

import java.util.Objects;

import com.comp2042.tetris.mechanics.bricks.BrickGenerator;
import com.comp2042.tetris.mechanics.bricks.BrickGeneratorFactory;
import com.comp2042.tetris.mechanics.movement.BrickPositionManager;
import com.comp2042.tetris.mechanics.rotation.BrickRotator;




public class DefaultBoardComponentsFactory implements BoardComponentsFactory {

    private final BrickGeneratorFactory generatorFactory;

    public DefaultBoardComponentsFactory(BrickGeneratorFactory generatorFactory) {
        this.generatorFactory = Objects.requireNonNull(generatorFactory, "generatorFactory must not be null");
    }

    @Override
    public BrickGenerator createGenerator() {
        return generatorFactory.create();
    }

    @Override
    public BrickRotator createRotator() {
        return new BrickRotator();
    }

    @Override
    public BrickPositionManager createPositionManager() {
        
        return new BrickPositionManager(4, 0);
    }
}

