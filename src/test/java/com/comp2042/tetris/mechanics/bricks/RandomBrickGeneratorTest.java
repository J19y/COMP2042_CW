package com.comp2042.tetris.mechanics.bricks;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class RandomBrickGeneratorTest {

    @Test
    void peekShowsNextBrickWithoutRemovingIt() {
        RandomBrickGenerator generator = new RandomBrickGenerator();

        Brick firstPeek = generator.peekNextBrick();
        Brick secondPeek = generator.peekNextBrick();

        // Peeking twice should return the very same instance sitting at the head of the queue
        assertNotNull(firstPeek);
        assertSame(firstPeek, secondPeek);
    }

    @Test
    void peekAndGetReturnSameBrickInstance() {
        RandomBrickGenerator generator = new RandomBrickGenerator();

        Brick peeked = generator.peekNextBrick();
        Brick fetched = generator.getBrick();

        // Whatever we peek should be exactly what gets dequeued next
        assertNotNull(peeked);
        assertSame(peeked, fetched);
    }

    @Test
    void generatorKeepsQueueFilled() {
        RandomBrickGenerator generator = new RandomBrickGenerator();

        // Grab several bricks to make sure the buffer keeps refilling itself
        for (int i = 0; i < 10; i++) {
            Brick current = generator.getBrick();
            assertNotNull(current);
            assertNotNull(generator.peekNextBrick());
        }
    }
}
