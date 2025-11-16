package com.comp2042.tetris.mechanics.board;

import com.comp2042.tetris.mechanics.movement.BrickPositionManager;
import com.comp2042.tetris.mechanics.piece.BrickGenerator;
import com.comp2042.tetris.mechanics.piece.BrickGeneratorFactory;
import com.comp2042.tetris.mechanics.rotation.BrickRotator;
import java.util.Objects;


// Default implementation that wires existing board collaborators together.

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
        return new BrickPositionManager(4, 10);
    }
}
