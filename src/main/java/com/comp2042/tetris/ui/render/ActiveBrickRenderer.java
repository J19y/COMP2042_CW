package com.comp2042.tetris.ui.render;

import com.comp2042.tetris.domain.model.ViewData;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Renders the currently falling Tetromino and its ghost aligned with the landing row.
 */
public final class ActiveBrickRenderer {
    private final int brickSize;
    private final GridPane brickPanel;
    private final GridPane ghostPanel;
    private Rectangle[][] rectangles;
    private Rectangle[][] ghostRectangles;

    public ActiveBrickRenderer(int brickSize, GridPane brickPanel, GridPane ghostPanel) {
        this.brickSize = brickSize;
        this.brickPanel = brickPanel;
        this.ghostPanel = ghostPanel;
    }

    public void initialize(ViewData brick) {
        if (brickPanel == null) {
            return;
        }
        brickPanel.getChildren().clear();
        if (ghostPanel != null) {
            ghostPanel.getChildren().clear();
        }

        int[][] brickData = brick.getBrickData();
        rectangles = new Rectangle[brickData.length][brickData[0].length];
        ghostRectangles = new Rectangle[brickData.length][brickData[0].length];

        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle ghost = createGhost();
                ghostRectangles[i][j] = ghost;
                if (ghostPanel != null) {
                    ghostPanel.add(ghost, j, i);
                }
            }
        }

        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(brickSize, brickSize);
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        updateColors(brickData);
        updatePosition(brick);
    }

    public void refresh(ViewData brick) {
        if (rectangles == null) {
            initialize(brick);
            return;
        }
        updateColors(brick.getBrickData());
        updatePosition(brick);
    }

    private void updatePosition(ViewData brick) {
        if (brickPanel == null) {
            return;
        }
        double cellWidth = brickSize + brickPanel.getHgap();
        double cellHeight = brickSize + brickPanel.getVgap();
        double x = brick.getxPosition() * cellWidth;
        brickPanel.setTranslateX(x);
        if (ghostPanel != null) {
            ghostPanel.setTranslateX(x);
        }

        double lockTranslateY = (brick.getGhostY() - 2) * cellHeight;
        double freeTranslateY = (brick.getyPosition() - 2) * cellHeight;
        int rowsToGhost = brick.getGhostY() - brick.getyPosition();
        brickPanel.setTranslateY(rowsToGhost <= 1 ? lockTranslateY : freeTranslateY);

        if (ghostPanel != null) {
            double ghostY = (brick.getGhostY() - 2) * cellHeight;
            ghostPanel.setTranslateY(ghostY);
            boolean ghostActive = brick.getGhostY() > brick.getyPosition();
            updateGhostVisibility(ghostActive);
            ghostPanel.setVisible(ghostActive);
        }
    }

    private void updateColors(int[][] brickData) {
        for (int i = 0; i < brickData.length && i < rectangles.length; i++) {
            for (int j = 0; j < brickData[i].length && j < rectangles[i].length; j++) {
                Rectangle rect = rectangles[i][j];
                if (rect != null) {
                    rect.setFill(com.comp2042.tetris.ui.theme.CellColor.fromValue(brickData[i][j]));
                    rect.setArcHeight(9);
                    rect.setArcWidth(9);
                    boolean isVisible = brickData[i][j] != 0;
                    rect.setVisible(isVisible);
                }
            }
        }
    }

    private Rectangle createGhost() {
        Rectangle ghost = new Rectangle(brickSize, brickSize);
        ghost.setFill(Color.WHITE);
        ghost.setOpacity(0.2);
        ghost.setArcHeight(9);
        ghost.setArcWidth(9);
        ghost.setVisible(false);
        return ghost;
    }

    private void updateGhostVisibility(boolean ghostActive) {
        if (ghostRectangles == null || rectangles == null) {
            return;
        }
        for (int i = 0; i < ghostRectangles.length; i++) {
            for (int j = 0; j < ghostRectangles[i].length; j++) {
                Rectangle ghost = ghostRectangles[i][j];
                Rectangle rect = rectangles[i][j];
                if (ghost != null) {
                    ghost.setVisible(ghostActive && rect != null && rect.isVisible());
                }
            }
        }
    }
}