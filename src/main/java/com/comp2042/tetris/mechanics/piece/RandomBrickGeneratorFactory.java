package com.comp2042.tetris.mechanics.piece;


// Default factory that produces RandomBrickGenerator instances.

public class RandomBrickGeneratorFactory implements BrickGeneratorFactory {
    @Override
    public BrickGenerator create() {
        return new RandomBrickGenerator();
    }
}
