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
                Rectangle ghost = ghostRectangles != null && i < ghostRectangles.length && j < ghostRectangles[i].length 
                        ? ghostRectangles[i][j] 
                        : null;
                
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
                        
                        // Apply matching ghost style with same appearance but reduced opacity
                        if (ghost != null) {
                            applyGhostStyle(ghost, neonColor);
                        }
                    } else {
                        rect.setFill(Color.TRANSPARENT);
                        rect.setStroke(null);
                        rect.setEffect(null);
                        rect.setVisible(false);
                        
                        if (ghost != null) {
                            ghost.setFill(Color.TRANSPARENT);
                            ghost.setStroke(null);
                            ghost.setEffect(null);
                            ghost.setVisible(false);
                        }
                    }
                }
            }
        }
    }

    private Rectangle createGhost() {
        Rectangle ghost = new Rectangle(brickSize, brickSize);
        ghost.setArcHeight(2);
        ghost.setArcWidth(2);
        ghost.setVisible(false);
        return ghost;
    }
    
    private void applyGhostStyle(Rectangle ghost, Color neonColor) {
        // Apply ghost styling matching the brick style exactly but with reduced opacity
        // Inner fill: same dark semi-transparent as brick
        ghost.setFill(Color.web("#000000", 0.3));
        
        // Stroke: same neon color as brick but at reduced opacity
        ghost.setStroke(neonColor);
        ghost.setStrokeWidth(2); // Same stroke width as brick
        ghost.setArcHeight(2);
        ghost.setArcWidth(2);
        
        // Apply same glow effect but with reduced opacity
        javafx.scene.effect.DropShadow primaryGlow = new javafx.scene.effect.DropShadow();
        primaryGlow.setColor(neonColor.deriveColor(0, 0.85, 0.80, 0.35)); // Reduced from 0.55 to 0.35
        primaryGlow.setRadius(6);
        primaryGlow.setSpread(0.2);
        primaryGlow.setOffsetX(0);
        primaryGlow.setOffsetY(0);
        
        javafx.scene.effect.DropShadow secondaryGlow = new javafx.scene.effect.DropShadow();
        secondaryGlow.setColor(neonColor.deriveColor(0, 0.80, 0.90, 0.15)); // Reduced from 0.30 to 0.15
        secondaryGlow.setRadius(3);
        secondaryGlow.setSpread(0.3);
        secondaryGlow.setOffsetX(0);
        secondaryGlow.setOffsetY(0);
        secondaryGlow.setInput(primaryGlow);
        
        ghost.setEffect(secondaryGlow);
        
        // Overall reduced opacity to make it clearly distinguishable from active brick
        ghost.setOpacity(0.6);
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