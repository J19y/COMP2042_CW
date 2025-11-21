package com.comp2042.tetris.domain.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Model class for managing the game score.
 * Uses JavaFX property binding for automatic UI updates.
 */
public final class Score {
    private final IntegerProperty score = new SimpleIntegerProperty(0);

     // Gets the observable score property for binding The score property
    public IntegerProperty scoreProperty() {
        return score;
    }

    
     // This method returns the current score
    public int getValue() {
        return score.get();
    }

    
     // Adds points to the current score
    public void add(int points) {
        if (points < 0) {
            // Prevent adding negative points
            throw new IllegalArgumentException("Cannot add negative points");
        }
        score.setValue(score.getValue() + points);
    }

    
     // Resets the score to zero
    public void reset() {
        score.setValue(0);
    }
}