package com.comp2042.tetris.ui.render;

import com.comp2042.tetris.domain.model.ViewData;

import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

/**
 * Handles rendering of the active falling brick.
 * Extracted from GuiController to follow Single Responsibility Principle.
 * This class is only responsible for displaying and updating the active brick's position and appearance.
 */
public final class ActiveBrickRenderer {
    
    private final int brickSize;
    private final GridPane brickPanel;
    private final GridPane gamePanel;
    private Rectangle[][] rectangles;
    private Rectangle[][] ghostRectangles;


    // Constructor for ActiveBrickRenderer.
    public ActiveBrickRenderer(int brickSize, GridPane brickPanel, GridPane gamePanel) {
        this.brickSize = brickSize;
        this.brickPanel = brickPanel;
        this.gamePanel = gamePanel;
    }

    // Initialize the brick panel with rectangles based on the brick data.
    public void initialize(ViewData brick) {
        brickPanel.getChildren().clear();
        int[][] brickData = brick.getBrickData();
        rectangles = new Rectangle[brickData.length][brickData[0].length];
        ghostRectangles = new Rectangle[brickData.length][brickData[0].length];
        
        // Create ghost pieces first (so they are behind)
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle ghost = new Rectangle(brickSize, brickSize);
                ghost.setFill(javafx.scene.paint.Color.WHITE);
                ghost.setOpacity(0.2);
                ghost.setArcHeight(9);
                ghost.setArcWidth(9);
                ghostRectangles[i][j] = ghost;
                if (brickPanel != null) {
                    brickPanel.add(ghost, j, i);
                }
            }
        }

        // Create active pieces
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(brickSize, brickSize);
                rectangle.setFill(com.comp2042.tetris.ui.theme.CellColor.fromValue(brickData[i][j]));
                rectangles[i][j] = rectangle;
                if (brickPanel != null) {
                    brickPanel.add(rectangle, j, i);
                }
            }
        }
        updatePosition(brick);
    }


    // Refresh the brick panel with new brick data and position.
    public void refresh(ViewData brick) {
        if (rectangles == null) {
            initialize(brick);
            return;
        }
        
        updatePosition(brick);
        updateColors(brick.getBrickData());
    }

    
    // Update the position of the brick panel based on the brick's coordinates.
    private void updatePosition(ViewData brick) {
        if (brickPanel == null || gamePanel == null) {
            return;
        }
        
        double x = brick.getxPosition() * (brickSize + brickPanel.getHgap());
        // Offset by 2 hidden rows so the active brick lines up with the visible board
        double y = (brick.getyPosition() - 2) * (brickSize + brickPanel.getVgap());
        
        brickPanel.setTranslateX(x);
        brickPanel.setTranslateY(y);
        
        // Update ghost piece position
        double ghostOffset = (brick.getGhostY() - brick.getyPosition()) * (brickSize + brickPanel.getVgap());
        
        for (Rectangle[] ghostRow : ghostRectangles) {
            for (Rectangle ghost : ghostRow) {
                if (ghost != null) {
                    ghost.setTranslateY(ghostOffset);
                    ghost.setVisible(ghostOffset > 0);
                }
            }
        }
    }

    
    // Update the colors of the rectangles based on the brick data.
    private void updateColors(int[][] brickData) {
        for (int i = 0; i < brickData.length && i < rectangles.length; i++) {
            for (int j = 0; j < brickData[i].length && j < rectangles[i].length; j++) {
                Rectangle rect = rectangles[i][j];
                Rectangle ghost = ghostRectangles[i][j];
                
                if (rect != null) {
                    javafx.scene.paint.Paint color = com.comp2042.tetris.ui.theme.CellColor.fromValue(brickData[i][j]);
                    rect.setFill(color);
                    rect.setArcHeight(9);
                    rect.setArcWidth(9);
                    
                    // Update ghost visibility based on brick shape
                    if (ghost != null) {
                        boolean isVisible = brickData[i][j] != 0;
                        ghost.setVisible(isVisible);
                        rect.setVisible(isVisible);
                    }
                }
            }
        }
    }
}
