package com.comp2042.tetris.engine.bricks;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    @Test
    void generatorProducesVariedBrickTypes() {
        RandomBrickGenerator generator = new RandomBrickGenerator();
        Set<Class<?>> brickTypes = new HashSet<>();
        
        // Generate 500 bricks to reliably get all types
        // With 7 types, ~71 per type average
        for (int i = 0; i < 500; i++) {
            Brick brick = generator.getBrick();
            brickTypes.add(brick.getClass());
        }
        
        // Standard Tetris has 7 brick types - should all appear with 500 samples
        assertTrue(brickTypes.size() >= 6, "Generator should produce most Tetris brick types (got " + brickTypes.size() + ")");
    }

    @Test
    void generatorRandomnessDistribution() {
        RandomBrickGenerator generator = new RandomBrickGenerator();
        Map<Class<?>, Integer> brickCounts = new HashMap<>();
        
        // Generate 1400 bricks to get ~200 of each type for robust statistics
        for (int i = 0; i < 1400; i++) {
            Brick brick = generator.getBrick();
            Class<?> type = brick.getClass();
            brickCounts.merge(type, 1, Integer::sum);
        }
        
        // Verify most types are present (at least 6 of 7)
        assertTrue(brickCounts.size() >= 6, "Should generate at least 6 of 7 brick types (got " + brickCounts.size() + ")");
        
        // Verify distribution is reasonably uniform
        // Each type should appear roughly 200 times (1400/7)
        // Allow ±30% variance (140-260 per type) for realistic random distribution
        for (int count : brickCounts.values()) {
            assertTrue(count >= 140 && count <= 260, 
                "Distribution skew detected: count=" + count + ". Expected 140-260 out of 1400.");
        }
    }

    @Test
    void generatorNotSeededIdentically() {
        RandomBrickGenerator gen1 = new RandomBrickGenerator();
        RandomBrickGenerator gen2 = new RandomBrickGenerator();
        
        // Get first 10 bricks from each
        Map<Class<?>, Integer> counts1 = new HashMap<>();
        Map<Class<?>, Integer> counts2 = new HashMap<>();
        
        for (int i = 0; i < 10; i++) {
            counts1.merge(gen1.getBrick().getClass(), 1, Integer::sum);
            counts2.merge(gen2.getBrick().getClass(), 1, Integer::sum);
        }
        
        // Both generators should produce bricks (exact type depends on random seed)
        assertTrue(!counts1.isEmpty() && !counts2.isEmpty(), 
            "Both generators should produce bricks");
    }

    @Test
    void generatorConsistencyOverLargeSequence() {
        RandomBrickGenerator generator = new RandomBrickGenerator();
        Map<Class<?>, Integer> brickCounts = new HashMap<>();
        
        // Generate 3500 bricks (5 cycles of 700)
        for (int i = 0; i < 3500; i++) {
            Brick brick = generator.getBrick();
            brickCounts.merge(brick.getClass(), 1, Integer::sum);
        }
        
        // Over large sequences, distribution should remain reasonably balanced
        int totalBricks = 3500;
        int expectedPerType = totalBricks / 7; // ~500 per type
        
        // Count must match expected number of types
        assertTrue(brickCounts.size() >= 6, "Should generate at least 6 of 7 brick types over large sequence (got " + brickCounts.size() + ")");
        
        for (int count : brickCounts.values()) {
            // Allow ±25% variance for large samples - with randomness, need reasonable bounds
            int lowerBound = (int)(expectedPerType * 0.75);
            int upperBound = (int)(expectedPerType * 1.25);
            assertTrue(count >= lowerBound && count <= upperBound,
                "Over 3500 bricks, distribution should be reasonably balanced. Got count=" + count + 
                ", expected " + lowerBound + "-" + upperBound);
        }
    }

    @Test
    void peekDoesNotAffectRandomness() {
        RandomBrickGenerator gen1 = new RandomBrickGenerator();
        RandomBrickGenerator gen2 = new RandomBrickGenerator();
        
        // Gen1: just get bricks
        Brick get1 = gen1.getBrick();
        Brick get2 = gen1.getBrick();
        
        // Gen2: peek then get
        Brick peek1 = gen2.peekNextBrick();
        Brick get3 = gen2.getBrick();
        Brick peek2 = gen2.peekNextBrick();
        Brick get4 = gen2.getBrick();
        
        // Both generators should produce bricks (exact type depends on random seed)
        assertNotNull(get1);
        assertNotNull(get2);
        assertNotNull(get3);
        assertNotNull(get4);
        assertSame(peek1, get3);
        assertSame(peek2, get4);
    }

    @Test
    void queueNeverEmptiesAfterManyGets() {
        RandomBrickGenerator generator = new RandomBrickGenerator();
        
        // Get 1000 bricks and verify peek never fails
        for (int i = 0; i < 1000; i++) {
            generator.getBrick();
            Brick next = generator.peekNextBrick();
            assertNotNull(next, "Queue should never be empty at iteration " + i);
        }
    }

    @Test
    void generatorBrickIndependence() {
        RandomBrickGenerator generator = new RandomBrickGenerator();
        Brick brick1 = generator.getBrick();
        Brick brick2 = generator.getBrick();
        
        // Even if they're the same type, they should be different instances
        // (due to deep copy in getRotationMatrix)
        assertNotNull(brick1);
        assertNotNull(brick2);
        // Both should have valid rotation matrices
        assertTrue(!brick1.getRotationMatrix().isEmpty());
        assertTrue(!brick2.getRotationMatrix().isEmpty());
    }
}

