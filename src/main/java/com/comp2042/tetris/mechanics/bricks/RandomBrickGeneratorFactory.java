package com.comp2042.tetris.mechanics.bricks;




public class RandomBrickGeneratorFactory implements BrickGeneratorFactory {
    @Override
    public BrickGenerator create() {
        return new RandomBrickGenerator();
    }
}

