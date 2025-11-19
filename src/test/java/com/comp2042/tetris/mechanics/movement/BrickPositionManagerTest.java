package com.comp2042.tetris.mechanics.movement;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import org.junit.jupiter.api.Test;


class BrickPositionManagerTest {

    @Test
    void constructorSetsInitialCoordinates() {
        BrickPositionManager manager = new BrickPositionManager(2, 3);

        // The manager should report the same coordinates we pass in
        assertEquals(2, manager.getX());
        assertEquals(3, manager.getY());
    }

    @Test
    void calculateMoveDownReturnsNewPoint() {
        BrickPositionManager manager = new BrickPositionManager(5, 1);

        // Checks if the calculated move down position is one row below the current position
        Point next = manager.calculateMoveDown();

        // Original position must stay intact
        assertEquals(5, manager.getX());
        assertEquals(1, manager.getY());
        // Returned point should be one row below
        assertEquals(5, next.x);
        assertEquals(2, next.y);
    }

    @Test
    void calculateMoveLeftAndRightReflectOffsets() {
        BrickPositionManager manager = new BrickPositionManager(4, 2);

        Point left = manager.calculateMoveLeft();
        Point right = manager.calculateMoveRight();

        // Left move should decrement x; right move should increment x
        assertEquals(3, left.x);
        assertEquals(5, right.x);
        // Y coordinate shouldn't change for horizontal calculations
        assertEquals(2, left.y);
        assertEquals(2, right.y);
    }

    @Test
    void getCurrentPositionReturnsDefensiveCopy() {
        BrickPositionManager manager = new BrickPositionManager(0, 0);

        Point current = manager.getCurrentPosition();
        current.translate(10, 10);

        // Ensures the returned Point is a copy, not the actual stored one.
        Point again = manager.getCurrentPosition();
        assertNotSame(current, again);
        assertEquals(0, again.x);
        assertEquals(0, again.y);
    }

    @Test
    void updatePositionFollowsProvidedPoint() {
        BrickPositionManager manager = new BrickPositionManager(1, 1);

        manager.updatePosition(new Point(9, 4));

        // Updates the internal position
        assertEquals(9, manager.getX());
        assertEquals(4, manager.getY());
    }

    // Updating from an external Point must copy to avoid aliasing side-effects.
    @Test
    void updatePositionCopiesProvidedPoint() {
        BrickPositionManager manager = new BrickPositionManager(0, 0);
        Point external = new Point(4, 5);

        manager.updatePosition(external);
        external.translate(10, 10);

        // Manager should not be affected by external mutations
        assertEquals(4, manager.getX());
        assertEquals(5, manager.getY());
    }

    @Test
    void resetMovesPieceToExactCoordinates() {
        BrickPositionManager manager = new BrickPositionManager(1, 1);

        manager.reset(7, 8);

        // Reset should be a simple setter for x/y
        assertEquals(7, manager.getX());
        assertEquals(8, manager.getY());
    }
}
