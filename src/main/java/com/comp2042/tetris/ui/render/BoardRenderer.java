package com.comp2042.tetris.ui.render;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import com.comp2042.tetris.ui.theme.ColorPalette;

/**
 * Renders the board background grid of cells.
 * Responsible only for creating and updating the static background matrix.
 * Now applies neon-glow effects to all brick cells.
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
        // Ensure the GridPane snaps to integer pixels to avoid sub-pixel gaps
        gamePanel.setSnapToPixel(true);
        Rectangle[][] displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(brickSize, brickSize);
                rectangle.setFill(com.comp2042.tetris.ui.theme.CellColor.fromValue(0));
                rectangle.setTranslateX(0);
                rectangle.setTranslateY(0);
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

    private void setRectangleData(int cellId, Rectangle rectangle) {
        if (rectangle != null) {
            // Apply neon glow if this is a non-empty cell
            if (cellId != 0) {
                Color baseColor = ColorPalette.getInstance().getColor(cellId) instanceof Color
                        ? (Color) ColorPalette.getInstance().getColor(cellId)
                        : Color.WHITE;
                Color neonColor = ColorPalette.getNeon(cellId);
                if (neonColor == null) {
                    neonColor = baseColor;
                }
                // For placed/background bricks we use a subtle stroke-only style
                // to avoid large glow effects that can visually overflow the board
                rectangle.setFill(Color.web("#000000", 0.28));
                rectangle.setStroke(neonColor);
                rectangle.setStrokeWidth(1);
                rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);
                rectangle.setEffect(null);
                rectangle.setVisible(true);
            } else {
                // Empty cell: no fill, no effects
                rectangle.setFill(com.comp2042.tetris.ui.theme.CellColor.fromValue(cellId));
                rectangle.setStroke(null);
                rectangle.setEffect(null);
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);
                rectangle.setVisible(false);
            }
        }
    }
}
