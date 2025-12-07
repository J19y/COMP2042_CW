package com.comp2042.tetris.ui.view;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * Renders the next brick preview panel.
 * <p>
 * Displays upcoming bricks with neon glow styling to show
 * players what pieces are coming next.
 * </p>
 *
 * @version 1.0
 */
public class NextBrickRenderer {

    private final VBox nextBrickPanel;
    private final int brickSize;

    public NextBrickRenderer(VBox nextBrickPanel, int brickSize) {
        this.nextBrickPanel = nextBrickPanel;
        this.brickSize = brickSize;
    }

    public void render(List<int[][]> nextBricks) {
        if (nextBrickPanel == null) {
            return;
        }
        nextBrickPanel.getChildren().clear();
        if (nextBricks == null || nextBricks.isEmpty()) {
            return;
        }
        int limit = Math.min(2, nextBricks.size());
        for (int idx = 0; idx < limit; idx++) {
            int[][] nextBrickData = nextBricks.get(idx);
            GridPane brickGrid = new GridPane();
            brickGrid.setHgap(1);
            brickGrid.setVgap(1);
            brickGrid.setAlignment(Pos.CENTER);
            for (int i = 0; i < nextBrickData.length; i++) {
                for (int j = 0; j < nextBrickData[i].length; j++) {
                    if (nextBrickData[i][j] != 0) {
                        Rectangle rectangle = new Rectangle(brickSize, brickSize);
                        javafx.scene.paint.Paint paint = com.comp2042.tetris.ui.theme.CellColor.fromValue(nextBrickData[i][j]);
                        rectangle.setFill(paint);
                        rectangle.setArcHeight(9);
                        rectangle.setArcWidth(9);
                        javafx.scene.paint.Paint basePaint = paint;
                        javafx.scene.paint.Color baseColor = javafx.scene.paint.Color.WHITE;
                        if (basePaint instanceof javafx.scene.paint.Color) {
                            baseColor = (javafx.scene.paint.Color) basePaint;
                        }
                        com.comp2042.tetris.ui.theme.NeonGlowStyle.applyPlacedStyle(rectangle, baseColor);
                        brickGrid.add(rectangle, j, i);
                    }
                }
            }
            nextBrickPanel.getChildren().add(brickGrid);
        }
    }
}
