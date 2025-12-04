package com.comp2042.tetris.domain.model;

import com.comp2042.tetris.util.MatrixOperations;
import java.util.List;
import java.util.ArrayList;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final List<int[][]> nextBrickData;
    private final int ghostY;

    public ViewData(int[][] brickData, int xPosition, int yPosition, List<int[][]> nextBrickData) {
        this(brickData, xPosition, yPosition, nextBrickData, yPosition);
    }

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
        this(brickData, xPosition, yPosition, convertToList(nextBrickData), yPosition);
    }

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

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }
    
    public int getGhostY() {
        return ghostY;
    }

    public List<int[][]> getNextBrickData() {
        List<int[][]> copy = new ArrayList<>();
        for (int[][] matrix : nextBrickData) {
            copy.add(MatrixOperations.copy(matrix));
        }
        return copy;
    }
}

