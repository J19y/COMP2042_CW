package com.comp2042.tetris.engine.bricks;




public class RandomBrickGeneratorFactory implements BrickGeneratorFactory {
    @Override
    public BrickGenerator create() {
        return new RandomBrickGenerator();
    }
}

