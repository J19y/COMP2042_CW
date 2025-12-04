package com.comp2042.tetris.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ScoreTest {

    @Test
    void scoreStartsAtZero() {
        Score score = new Score();

        
        assertEquals(0, score.getValue());
        assertEquals(0, score.scoreProperty().get());
    }

    @Test
    void addPointsAccumulatesValue() {
        Score score = new Score();

        score.add(5);
        score.add(3);

        
        assertEquals(8, score.getValue());
    }

    @Test
    void addRejectsNegativeValues() {
        Score score = new Score();

        
        assertThrows(IllegalArgumentException.class, () -> score.add(-1));
    }

    @Test
    void resetClearsAccumulatedPoints() {
        Score score = new Score();
        score.add(10);

        score.reset();

        
        assertEquals(0, score.getValue());
        assertEquals(0, score.scoreProperty().get());
    }
}

