package com.comp2042.tetris.engine.bricks;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class RandomBrickGeneratorTest {

    @Test
    void peekShowsNextBrickWithoutRemovingIt() {
        RandomBrickGenerator generator = new RandomBrickGenerator();

        Brick firstPeek = generator.peekNextBrick();
        Brick secondPeek = generator.peekNextBrick();

        
        assertNotNull(firstPeek);
        assertSame(firstPeek, secondPeek);
    }

    @Test
    void peekAndGetReturnSameBrickInstance() {
        RandomBrickGenerator generator = new RandomBrickGenerator();

        Brick peeked = generator.peekNextBrick();
        Brick fetched = generator.getBrick();

        
        assertNotNull(peeked);
        assertSame(peeked, fetched);
    }

    @Test
    void generatorKeepsQueueFilled() {
        RandomBrickGenerator generator = new RandomBrickGenerator();

        
        for (int i = 0; i < 10; i++) {
            Brick current = generator.getBrick();
            assertNotNull(current);
            assertNotNull(generator.peekNextBrick());
        }
    }
}

