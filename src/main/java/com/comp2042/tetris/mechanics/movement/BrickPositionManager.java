package com.comp2042.tetris.mechanics.movement;

import java.awt.Point;

/**
 * Manages the active brick's position on the board.
 * Extracted from SimpleBoard to follow Single Responsibility Principle.
 * Responsible only for tracking and calculating brick positions.
 */
public final class BrickPositionManager {
    
    private Point activePiece;

    // Creates a new BrickPositionManager with initial position.
    public BrickPositionManager(int startX, int startY) {
        this.activePiece = new Point(startX, startY);
    }

    // Get the current position of the active brick.
    public Point getCurrentPosition() {
        return new Point(activePiece);
    }

    
    // Calculate the position if the brick moves down.
    public Point calculateMoveDown() {
        Point newPos = new Point(activePiece);
        newPos.translate(0, 1);
        return newPos;
    }

    
    // Calculate the position if the brick moves left.
    public Point calculateMoveLeft() {
        Point newPos = new Point(activePiece);
        newPos.translate(-1, 0);
        return newPos;
    }

    
    // Calculate the position if the brick moves right.
    public Point calculateMoveRight() {
        Point newPos = new Point(activePiece);
        newPos.translate(1, 0);
        return newPos;
    }

    
    // Update the brick's position.
    public void updatePosition(Point newPosition) {
        this.activePiece = new Point(newPosition);
    }

    
    // Reset the brick position to specified coordinates.
    public void reset(int x, int y) {
        this.activePiece = new Point(x, y);
    }

    
    // Get the x coordinate of the current position.
    public int getX() {
        return (int) activePiece.getX();
    }

    // Get the y coordinate of the current position.
    public int getY() {
        return (int) activePiece.getY();
    }
}
