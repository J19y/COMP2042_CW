package com.comp2042.tetris.ui.render;

import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

/**
 * Renders the board background grid of cells.
 * Responsible only for creating and updating the static background matrix.
 */
public final class BoardRenderer {

    private final int brickSize;

    public BoardRenderer(int brickSize) {
        this.brickSize = brickSize;
    }

    /**
     * Initializes the board background on the provided GridPane.
     * Skips the first two rows (hidden rows), consistent with previous behavior.
     * Returns the display matrix of Rectangles for later refresh operations.
     */
    public Rectangle[][] initBoard(GridPane gamePanel, int[][] boardMatrix) {
        gamePanel.getChildren().clear(); // Clear existing children to prevent duplicates/overflow
        Rectangle[][] displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(brickSize, brickSize);
                rectangle.setFill(com.comp2042.tetris.ui.theme.CellColor.fromValue(0));
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }
        return displayMatrix;
    }

    /**
     * Refreshes the colors of the background cells using the provided matrix.
     */
    
    public void refreshBoard(int[][] board, Rectangle[][] displayMatrix) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        if (rectangle != null) {
            rectangle.setFill(com.comp2042.tetris.ui.theme.CellColor.fromValue(color));
            rectangle.setArcHeight(9);
            rectangle.setArcWidth(9);
        }
    }
}
