package com.comp2042.service;

import com.comp2042.model.Score;

import javafx.beans.property.IntegerProperty;

/**
 * It manages the player's score during the game.
 * This service provides methods to add points, reset the score,
 * and retrieve the current score value.
 */
public final class ScoreService {
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
