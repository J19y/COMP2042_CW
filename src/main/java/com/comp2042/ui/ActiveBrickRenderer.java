package com.comp2042.ui;

import com.comp2042.model.ViewData;

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


    // Constructor for ActiveBrickRenderer.
    public ActiveBrickRenderer(int brickSize, GridPane brickPanel, GridPane gamePanel) {
        this.brickSize = brickSize;
        this.brickPanel = brickPanel;
        this.gamePanel = gamePanel;
    }

    // Initialize the brick panel with rectangles based on the brick data.
    public void initialize(ViewData brick) {
        int[][] brickData = brick.getBrickData();
        rectangles = new Rectangle[brickData.length][brickData[0].length];
        
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(brickSize, brickSize);
                rectangle.setFill(CellColor.fromValue(brickData[i][j]));
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
        
        double layoutX = gamePanel.getLayoutX() + 
            brick.getxPosition() * brickPanel.getVgap() + 
            brick.getxPosition() * brickSize;
        double layoutY = -42 + gamePanel.getLayoutY() + 
            brick.getyPosition() * brickPanel.getHgap() + 
            brick.getyPosition() * brickSize;
        
        brickPanel.setLayoutX(layoutX);
        brickPanel.setLayoutY(layoutY);
    }

    
    // Update the colors of the rectangles based on the brick data.
    private void updateColors(int[][] brickData) {
        for (int i = 0; i < brickData.length && i < rectangles.length; i++) {
            for (int j = 0; j < brickData[i].length && j < rectangles[i].length; j++) {
                Rectangle rect = rectangles[i][j];
                if (rect != null) {
                    rect.setFill(CellColor.fromValue(brickData[i][j]));
                    rect.setArcHeight(9);
                    rect.setArcWidth(9);
                }
            }
        }
    }
}
