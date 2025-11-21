package com.comp2042.tetris.mechanics.rotation;

import com.comp2042.tetris.domain.model.RotationInfo;
import com.comp2042.tetris.mechanics.bricks.Brick;


// Handles brick rotation logic and maintains the current brick state.
public class BrickRotator {
    private Brick brick;
    private int currentShape = 0;
    
    // Returns the index of the current rotation shape
    public int getCurrentShapeIndex() {
        return currentShape;
    }

    /**
     * Returns information about the next rotation for the current brick.
     * Renamed from 'getShapeMatrix' -> 'getRotationMatrix' for clarity that this
     * method returns all rotation matrices for a brick.
     */
    public RotationInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getRotationMatrix().size();
        return new RotationInfo(brick.getRotationMatrix().get(nextShape), nextShape);
    }

    public int[][] getCurrentShape() {
        return brick.getRotationMatrix().get(currentShape);
    }

    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    public void setBrick(Brick brick) {
        if (brick == null || brick.getRotationMatrix() == null || brick.getRotationMatrix().isEmpty()) {
            throw new IllegalArgumentException("Brick must have at least one rotation matrix");
        }
        this.brick = brick;
        currentShape = 0;
    }
}