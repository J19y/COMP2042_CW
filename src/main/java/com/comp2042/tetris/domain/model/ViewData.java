package com.comp2042.tetris.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.comp2042.tetris.util.MatrixOperations;

/**
 * Immutable data transfer object carrying brick shape, position, ghost position, and next brick queue.
 * <p>
 * Used to transfer view-related data between the game engine and the UI layer.
 * All matrix data is defensively copied to maintain immutability.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final List<int[][]> nextBrickData;
    private final int ghostY;

    /**
     * Constructs a ViewData with brick data, position, and next brick queue.
     * Ghost Y position defaults to the current Y position.
     *
     * @param brickData the current brick shape matrix
     * @param xPosition the x-coordinate of the brick
     * @param yPosition the y-coordinate of the brick
     * @param nextBrickData list of next brick shape matrices
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, List<int[][]> nextBrickData) {
        this(brickData, xPosition, yPosition, nextBrickData, yPosition);
    }

    /**
     * Constructs a ViewData with brick data, position, and a single next brick.
     *
     * @param brickData the current brick shape matrix
     * @param xPosition the x-coordinate of the brick
     * @param yPosition the y-coordinate of the brick
     * @param nextBrickData the next brick shape matrix
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
        this(brickData, xPosition, yPosition, convertToList(nextBrickData), yPosition);
    }

    /**
     * Constructs a ViewData with all parameters including ghost position.
     *
     * @param brickData the current brick shape matrix
     * @param xPosition the x-coordinate of the brick
     * @param yPosition the y-coordinate of the brick
     * @param nextBrickData list of next brick shape matrices
     * @param ghostY the y-coordinate for the ghost piece preview
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, List<int[][]> nextBrickData, int ghostY) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.ghostY = ghostY;
    }

    private static List<int[][]> convertToList(int[][] matrix) {
        java.util.List<int[][]> list = new ArrayList<>();
        if (matrix != null) {
            list.add(MatrixOperations.copy(matrix));
        }
        return list;
    }

    /**
     * Returns a defensive copy of the brick shape data.
     *
     * @return a copy of the brick matrix
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Gets the x-coordinate (column) of the brick position.
     *
     * @return the x position
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Gets the y-coordinate (row) of the brick position.
     *
     * @return the y position
     */
    public int getyPosition() {
        return yPosition;
    }
    
    /**
     * Gets the y-coordinate for the ghost piece preview.
     *
     * @return the ghost y position
     */
    public int getGhostY() {
        return ghostY;
    }

    /**
     * Returns a defensive copy of the next brick queue.
     *
     * @return a list of copies of the next brick matrices
     */
    public List<int[][]> getNextBrickData() {
        List<int[][]> copy = new ArrayList<>();
        for (int[][] matrix : nextBrickData) {
            copy.add(MatrixOperations.copy(matrix));
        }
        return copy;
    }
}

