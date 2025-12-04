package com.comp2042.tetris.domain.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public final class Score {
    private final IntegerProperty score = new SimpleIntegerProperty(0);

     
    public IntegerProperty scoreProperty() {
        return score;
    }

    
     
    public int getValue() {
        return score.get();
    }

    
     
    public void add(int points) {
        if (points < 0) {
            
            throw new IllegalArgumentException("Cannot add negative points");
        }
        score.setValue(score.getValue() + points);
    }

    
     
    public void reset() {
        score.setValue(0);
    }
}
