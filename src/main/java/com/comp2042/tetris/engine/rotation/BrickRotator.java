package com.comp2042.tetris.engine.rotation;

import com.comp2042.tetris.domain.model.RotationInfo;
import com.comp2042.tetris.engine.bricks.Brick;



public class BrickRotator {
    private Brick brick;
    private int currentShape = 0;
    
    
    public int getCurrentShapeIndex() {
        return currentShape;
    }

    
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
