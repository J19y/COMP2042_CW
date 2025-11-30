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
        
        assertEquals(0, policy.scoreForLineClear(0));
        assertEquals(0, policy.scoreForLineClear(-3));
    }

    @Test
    void lineClearUsesQuadraticBonusTable() {
        
        assertEquals(50, policy.scoreForLineClear(1));
        assertEquals(200, policy.scoreForLineClear(2));
        assertEquals(800, policy.scoreForLineClear(4));
    }

    @Test
    void manualSoftDropAwardsOnePointPerTick() {
        
        assertEquals(1, policy.scoreForDrop(EventSource.USER, true));
    }

    @Test
    void gravityOrFailedDropGivesZeroPoints() {
        
        assertEquals(0, policy.scoreForDrop(EventSource.THREAD, true));
        assertEquals(0, policy.scoreForDrop(EventSource.USER, false));
    }
}

