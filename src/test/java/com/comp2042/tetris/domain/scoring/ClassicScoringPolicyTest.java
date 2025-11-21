package com.comp2042.tetris.domain.scoring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.comp2042.tetris.ui.input.EventSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassicScoringPolicyTest {

    private ClassicScoringPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new ClassicScoringPolicy();
    }

    @Test
    void zeroOrNegativeLineClearScoresNothing() {
        // Guard rails: bogus inputs should never produce points.
        assertEquals(0, policy.scoreForLineClear(0));
        assertEquals(0, policy.scoreForLineClear(-3));
    }

    @Test
    void lineClearUsesQuadraticBonusTable() {
        // Classic rules: 1 line = 50, 2 = 200, 4 = 800.
        assertEquals(50, policy.scoreForLineClear(1));
        assertEquals(200, policy.scoreForLineClear(2));
        assertEquals(800, policy.scoreForLineClear(4));
    }

    @Test
    void manualSoftDropAwardsOnePointPerTick() {
        // Any successful user-driven drop tick should pay out one point.
        assertEquals(1, policy.scoreForDrop(EventSource.USER, true));
    }

    @Test
    void gravityOrFailedDropGivesZeroPoints() {
        // Thread-driven gravity or blocked moves should not add score.
        assertEquals(0, policy.scoreForDrop(EventSource.THREAD, true));
        assertEquals(0, policy.scoreForDrop(EventSource.USER, false));
    }
}
