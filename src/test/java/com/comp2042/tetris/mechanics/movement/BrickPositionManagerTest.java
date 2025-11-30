package com.comp2042.tetris.mechanics.movement;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import org.junit.jupiter.api.Test;


class BrickPositionManagerTest {

    @Test
    void constructorSetsInitialCoordinates() {
        BrickPositionManager manager = new BrickPositionManager(2, 3);

        
        assertEquals(2, manager.getX());
        assertEquals(3, manager.getY());
    }

    @Test
    void calculateMoveDownReturnsNewPoint() {
        BrickPositionManager manager = new BrickPositionManager(5, 1);

        
        Point next = manager.calculateMoveDown();

        
        assertEquals(5, manager.getX());
        assertEquals(1, manager.getY());
        
        assertEquals(5, next.x);
        assertEquals(2, next.y);
    }

    @Test
    void calculateMoveLeftAndRightReflectOffsets() {
        BrickPositionManager manager = new BrickPositionManager(4, 2);

        Point left = manager.calculateMoveLeft();
        Point right = manager.calculateMoveRight();

        
        assertEquals(3, left.x);
        assertEquals(5, right.x);
        
        assertEquals(2, left.y);
        assertEquals(2, right.y);
    }

    @Test
    void getCurrentPositionReturnsDefensiveCopy() {
        BrickPositionManager manager = new BrickPositionManager(0, 0);

        Point current = manager.getCurrentPosition();
        current.translate(10, 10);

        
        Point again = manager.getCurrentPosition();
        assertNotSame(current, again);
        assertEquals(0, again.x);
        assertEquals(0, again.y);
    }

    @Test
    void updatePositionFollowsProvidedPoint() {
        BrickPositionManager manager = new BrickPositionManager(1, 1);

        manager.updatePosition(new Point(9, 4));

        
        assertEquals(9, manager.getX());
        assertEquals(4, manager.getY());
    }

    
    @Test
    void updatePositionCopiesProvidedPoint() {
        BrickPositionManager manager = new BrickPositionManager(0, 0);
        Point external = new Point(4, 5);

        manager.updatePosition(external);
        external.translate(10, 10);

        
        assertEquals(4, manager.getX());
        assertEquals(5, manager.getY());
    }

    @Test
    void resetMovesPieceToExactCoordinates() {
        BrickPositionManager manager = new BrickPositionManager(1, 1);

        manager.reset(7, 8);

        
        assertEquals(7, manager.getX());
        assertEquals(8, manager.getY());
    }
}

