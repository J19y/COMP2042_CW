package com.comp2042.game;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.model.RotationInfo;

/**
 * Handles brick rotation logic and maintains the current brick state.
 */
public class BrickRotator {
    private Brick brick;
    private int currentShape = 0;

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
        this.brick = brick;
        currentShape = 0;
    }
}