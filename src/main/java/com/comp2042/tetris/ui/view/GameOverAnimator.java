package com.comp2042.tetris.ui.view;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GameOverAnimator {

    private final GridPane gamePanel;
    private final GameOverPanel gameOverPanel;
    private final GaussianBlur backdropBlur = new GaussianBlur(0);
    private final ColorAdjust backdropDarken = new ColorAdjust();
    private Timeline backdropTimeline;

    public GameOverAnimator(GridPane gamePanel, GameOverPanel gameOverPanel) {
        this.gamePanel = gamePanel;
        this.gameOverPanel = gameOverPanel;
        backdropBlur.setInput(backdropDarken);
    }

    public void playDigitalFragmentationSequence(Rectangle[][] displayMatrix, int finalScore, int totalLines, long gameTime) {
        
        
        
        
        Timeline glitch = new Timeline(
            new KeyFrame(Duration.ZERO, e -> {
                gamePanel.setEffect(new BoxBlur(2, 2, 1));
                gamePanel.setTranslateX(-5);
            }),
            new KeyFrame(Duration.millis(50), e -> {
                gamePanel.setEffect(new ColorAdjust(0, 1.0, 0, 0)); 
                gamePanel.setTranslateX(5);
            }),
            new KeyFrame(Duration.millis(100), e -> {
                gamePanel.setEffect(new BoxBlur(5, 1, 1));
                gamePanel.setTranslateX(-5);
            }),
            new KeyFrame(Duration.millis(150), e -> {
                gamePanel.setEffect(null);
                gamePanel.setTranslateX(0);
            })
        );
        glitch.setCycleCount(2);
        
        
        glitch.setOnFinished(e -> startDissolveCascade(displayMatrix, finalScore, totalLines, gameTime));
        glitch.play();
    }

    private void startDissolveCascade(Rectangle[][] displayMatrix, int finalScore, int totalLines, long gameTime) {
        if (displayMatrix == null) return;

        
        Timeline cascade = new Timeline();
        double delayPerRow = 0.05; 

        for (int y = 0; y < displayMatrix.length; y++) {
            final int row = y;
            KeyFrame kf = new KeyFrame(Duration.seconds(y * delayPerRow), e -> dissolveRow(row, displayMatrix));
            cascade.getKeyFrames().add(kf);
        }
        
           cascade.setOnFinished(e -> {
               playBackdropTransition();
               if (gameOverPanel != null) gameOverPanel.show(finalScore, totalLines, gameTime);
           });
        cascade.play();
    }

    private void dissolveRow(int row, Rectangle[][] displayMatrix) {
        if (row >= displayMatrix.length) return;
        
        for (int x = 0; x < displayMatrix[row].length; x++) {
            Rectangle block = displayMatrix[row][x];
            
            if (block != null && block.getFill() != null && 
                !block.getFill().equals(com.comp2042.tetris.ui.theme.CellColor.fromValue(0))) {
                
                spawnPixels(block);
                block.setVisible(false);
            }
        }
    }

    private void spawnPixels(Rectangle block) {
        Bounds bounds = block.getBoundsInParent();
        double size = block.getWidth();
        int split = 2; 
        double pixelSize = size / split;
        
        
        javafx.scene.paint.Paint originalFill = block.getFill();
        javafx.scene.effect.Effect originalEffect = block.getEffect();
        
        for(int i=0; i<split; i++) {
            for(int j=0; j<split; j++) {
                Rectangle pixel = new Rectangle(pixelSize, pixelSize);
                
                pixel.setFill(originalFill != null ? originalFill : block.getFill());
                pixel.setStroke(block.getStroke());
                pixel.setStrokeWidth(block.getStrokeWidth());
                pixel.setArcHeight(block.getArcHeight());
                pixel.setArcWidth(block.getArcWidth());
                
                pixel.setManaged(false); 
                pixel.setTranslateX(bounds.getMinX() + i * pixelSize);
                pixel.setTranslateY(bounds.getMinY() + j * pixelSize);
                
                
                if (originalEffect != null) {
                    pixel.setEffect(originalEffect);
                } else {
                    pixel.setEffect(new Glow(0.8));
                }
                
                gamePanel.getChildren().add(pixel);
                animatePixel(pixel);
            }
        }
    }
    
    private void animatePixel(Rectangle pixel) {
        TranslateTransition fall = new TranslateTransition(Duration.seconds(0.8 + Math.random() * 0.5), pixel);
        fall.setByY(600); 
        fall.setByX((Math.random() - 0.5) * 40); 
        fall.setInterpolator(javafx.animation.Interpolator.EASE_IN);
        
        FadeTransition fade = new FadeTransition(Duration.seconds(0.8), pixel);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.millis(200));
        
        ParallelTransition pt = new ParallelTransition(fall, fade);
        pt.setOnFinished(e -> gamePanel.getChildren().remove(pixel));
        pt.play();
    }

    private void playBackdropTransition() {
        if (gamePanel == null) return;
        if (backdropTimeline != null) {
            backdropTimeline.stop();
        }
        backdropBlur.setRadius(0);
        backdropDarken.setBrightness(0);
        backdropDarken.setSaturation(0);
        gamePanel.setEffect(backdropBlur);

        backdropTimeline = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(backdropBlur.radiusProperty(), 0, Interpolator.EASE_BOTH),
                new KeyValue(backdropDarken.brightnessProperty(), 0, Interpolator.EASE_BOTH),
                new KeyValue(backdropDarken.saturationProperty(), 0, Interpolator.EASE_BOTH)
            ),
            new KeyFrame(Duration.seconds(1.3),
                new KeyValue(backdropBlur.radiusProperty(), 18, Interpolator.EASE_BOTH),
                new KeyValue(backdropDarken.brightnessProperty(), -0.45, Interpolator.EASE_BOTH),
                new KeyValue(backdropDarken.saturationProperty(), -0.3, Interpolator.EASE_BOTH)
            )
        );
        backdropTimeline.play();
    }

    void resetBackdropEffects() {
        if (backdropTimeline != null) {
            backdropTimeline.stop();
        }
        backdropBlur.setRadius(0);
        backdropDarken.setBrightness(0);
        backdropDarken.setSaturation(0);
        if (gamePanel != null) {
            gamePanel.setEffect(null);
        }
    }
}

