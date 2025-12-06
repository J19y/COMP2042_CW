package com.comp2042.tetris.engine.movement;

import java.awt.Point;

/**
 * Manages the position of the active brick on the game board.
 * Provides methods to calculate movement deltas and update position.
 * Uses Point coordinates where (0,0) is top-left of the board.
 *
 */
public final class BrickPositionManager {
    
    private Point activePiece;

    
    public BrickPositionManager(int startX, int startY) {
        this.activePiece = new Point(startX, startY);
    }

    
    public Point getCurrentPosition() {
        return new Point(activePiece);
    }

    
    
    public Point calculateMoveDown() {
        Point newPos = new Point(activePiece);
        newPos.translate(0, 1);
        return newPos;
    }

    
    
    public Point calculateMoveLeft() {
        Point newPos = new Point(activePiece);
        newPos.translate(-1, 0);
        return newPos;
    }

    
    
    public Point calculateMoveRight() {
        Point newPos = new Point(activePiece);
        newPos.translate(1, 0);
        return newPos;
    }

    
    
    public void updatePosition(Point newPosition) {
        this.activePiece = new Point(newPosition);
    }

    
    
    public void reset(int x, int y) {
        this.activePiece = new Point(x, y);
    }

    
    
    public int getX() {
        return (int) activePiece.getX();
    }

    
    public int getY() {
        return (int) activePiece.getY();
    }
}

