package com.comp2042.tetris.ui.render;

import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.ui.theme.ColorPalette;
import com.comp2042.tetris.ui.theme.NeonGlowStyle;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Renders the active (falling) brick and its ghost preview.
 * Manages two GridPanes: one for the actual brick with neon glow effects,
 * and one for the ghost showing where the brick will land.
 * Includes settle animation for visual feedback on brick placement.
 *
 */
public final class ActiveBrickRenderer {
    private final int brickSize;
    private final GridPane ghostPanel;
    private final GridPane brickPanel;
    private Rectangle[][] rectangles;
    private Rectangle[][] ghostRectangles;

    public ActiveBrickRenderer(int brickSize, GridPane ghostPanel, GridPane brickPanel) {
        this.brickSize = brickSize;
        this.ghostPanel = ghostPanel;
        this.brickPanel = brickPanel;
    }

    public void initialize(ViewData brick) {
        if (brickPanel == null) {
            return;
        }
        brickPanel.getChildren().clear();
        if (ghostPanel != null) ghostPanel.getChildren().clear();

        int[][] brickData = brick.getBrickData();
        rectangles = new Rectangle[brickData.length][brickData[0].length];
        ghostRectangles = new Rectangle[brickData.length][brickData[0].length];

        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle gRect = new Rectangle(brickSize, brickSize);
                gRect.setVisible(false);
                ghostRectangles[i][j] = gRect;
                if (ghostPanel != null) ghostPanel.add(gRect, j, i);

                Rectangle rect = new Rectangle(brickSize, brickSize);
                rectangles[i][j] = rect;
                brickPanel.add(rect, j, i);
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

    
    public void animateSettle(Runnable onFinished) {
        if (brickPanel == null) {
            if (onFinished != null) onFinished.run();
            return;
        }

        javafx.animation.TranslateTransition tt = new javafx.animation.TranslateTransition(javafx.util.Duration.millis(120), brickPanel);
        tt.setFromY(-6);
        tt.setToY(0);
        tt.setInterpolator(javafx.animation.Interpolator.EASE_IN);

        javafx.animation.ScaleTransition st = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(120), brickPanel);
        st.setFromX(0.96);
        st.setFromY(0.96);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        javafx.animation.ParallelTransition pt = new javafx.animation.ParallelTransition(tt, st);
        pt.setOnFinished(e -> {
            brickPanel.setTranslateY(0);
            brickPanel.setScaleX(1.0);
            brickPanel.setScaleY(1.0);
            if (onFinished != null) onFinished.run();
        });
        pt.play();
    }

    private void updatePosition(ViewData brick) {
        if (brickPanel == null) {
            return;
        }
        double cellWidth = brickSize + brickPanel.getHgap();
        double cellHeight = brickSize + brickPanel.getVgap();
        double x = brick.getxPosition() * cellWidth;
        
        double tx = Math.round(x);
        brickPanel.setTranslateX(tx);
        if (ghostPanel != null) ghostPanel.setTranslateX(tx);

        double freeTranslateY = (brick.getyPosition() - 2) * cellHeight;
        brickPanel.setTranslateY(Math.round(freeTranslateY));

        
        int ghostY = brick.getGhostY();
        double ghostTranslateY = (ghostY - 2) * cellHeight;
        if (ghostPanel != null) {
            ghostPanel.setTranslateY(Math.round(ghostTranslateY));
        }
        brickPanel.setSnapToPixel(true);
        
    }

    private void updateColors(int[][] brickData) {
        for (int i = 0; i < brickData.length && i < rectangles.length; i++) {
            for (int j = 0; j < brickData[i].length && j < rectangles[i].length; j++) {
                Rectangle rect = rectangles[i][j];
                Rectangle gRect = (ghostRectangles != null) ? ghostRectangles[i][j] : null;

                if (rect != null) {
                    if (brickData[i][j] != 0) {
                        
                        Color baseColor = ColorPalette.getInstance().getColor(brickData[i][j]) instanceof Color
                                ? (Color) ColorPalette.getInstance().getColor(brickData[i][j])
                                : Color.WHITE;
                        Color neonColor = ColorPalette.getNeon(brickData[i][j]);
                        if (neonColor == null) {
                            neonColor = baseColor;
                        }
                        
                        NeonGlowStyle.applyNeonGlow(rect, baseColor, neonColor);
                        rect.setVisible(true);

                        
                        if (gRect != null) {
                            NeonGlowStyle.applyGhostNeon(gRect, baseColor, neonColor);
                            gRect.setVisible(true);
                        }
                    } else {
                        rect.setFill(Color.TRANSPARENT);
                        rect.setStroke(null);
                        rect.setEffect(null);
                        rect.setVisible(false);
                        if (gRect != null) {
                            gRect.setVisible(false);
                        }
                    }
                }
            }
        }
    }
    
    
}
