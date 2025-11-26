package com.comp2042.tetris.ui.render;

import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.ui.theme.ColorPalette;
import com.comp2042.tetris.ui.theme.NeonGlowStyle;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Renders the currently falling Tetromino and its ghost aligned with the landing row.
 * Applies neon-glow effects to all active bricks for a cohesive visual style.
 * Ghost bricks match the neon style but with reduced opacity for clear distinction.
 */
public final class ActiveBrickRenderer {
    private final int brickSize;
    private final GridPane brickPanel;
    private Rectangle[][] rectangles;
    // Ghost functionality has been removed — parameter retained for compatibility

    public ActiveBrickRenderer(int brickSize, GridPane brickPanel) {
        this.brickSize = brickSize;
        this.brickPanel = brickPanel;
    }

    public void initialize(ViewData brick) {
        if (brickPanel == null) {
            return;
        }
        brickPanel.getChildren().clear();

        int[][] brickData = brick.getBrickData();
        rectangles = new Rectangle[brickData.length][brickData[0].length];

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
        double freeTranslateY = (brick.getyPosition() - 2) * cellHeight;
        brickPanel.setTranslateY(freeTranslateY);
        // Ghost is intentionally disabled: do not translate or show ghostPanel
    }

    private void updateColors(int[][] brickData) {
        for (int i = 0; i < brickData.length && i < rectangles.length; i++) {
            for (int j = 0; j < brickData[i].length && j < rectangles[i].length; j++) {
                Rectangle rect = rectangles[i][j];

                if (rect != null) {
                    if (brickData[i][j] != 0) {
                        // Apply neon glow to active brick
                        Color baseColor = ColorPalette.getInstance().getColor(brickData[i][j]) instanceof Color
                                ? (Color) ColorPalette.getInstance().getColor(brickData[i][j])
                                : Color.WHITE;
                        Color neonColor = ColorPalette.getNeon(brickData[i][j]);
                        if (neonColor == null) {
                            neonColor = baseColor;
                        }
                        NeonGlowStyle.applyNeonGlow(rect, baseColor, neonColor);
                        rect.setVisible(true);
                    } else {
                        rect.setFill(Color.TRANSPARENT);
                        rect.setStroke(null);
                        rect.setEffect(null);
                        rect.setVisible(false);
                    }
                }
            }
        }
    }
    
    // Ghost-related methods removed — ghost intentionally disabled
}