package com.comp2042.tetris.domain.scoring;

import com.comp2042.tetris.domain.model.Score;

import javafx.beans.property.IntegerProperty;


public final class ScoreManager {
    private final Score score = new Score();

    public IntegerProperty scoreProperty() {
        return score.scoreProperty();
    }

    public int getValue() {
        return score.getValue();
    }

    public void add(int points) {
        score.add(points);
    }

    public void reset() {
        score.reset();
    }
}

