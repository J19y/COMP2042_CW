package com.comp2042.tetris.ui.theme;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public final class NeonGlowStyle {

    private static final int STROKE_WIDTH = 1;
    private static final int PRIMARY_GLOW_RADIUS = 4;
    private static final double PRIMARY_GLOW_SPREAD = 0.18;
    private static final int SECONDARY_GLOW_RADIUS = 2;
    private static final double SECONDARY_GLOW_SPREAD = 0.25;
    private static final int ARC_RADIUS = 9;

    private NeonGlowStyle() {}

    
    public static void applyNeonGlow(Rectangle rectangle, Color baseColor, Color neonColor) {
        if (rectangle == null || baseColor == null || neonColor == null) {
            return;
        }

        
        rectangle.setFill(Color.web("#000000", 0.3));

        
        rectangle.setStroke(neonColor);
        rectangle.setStrokeWidth(STROKE_WIDTH);
        rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

        
        rectangle.setArcHeight(ARC_RADIUS);
        rectangle.setArcWidth(ARC_RADIUS);

        
        DropShadow primaryGlow = new DropShadow();
        primaryGlow.setColor(neonColor.deriveColor(0, 0.85, 0.80, 0.55)); 
        primaryGlow.setRadius(PRIMARY_GLOW_RADIUS);
        primaryGlow.setSpread(PRIMARY_GLOW_SPREAD);
        primaryGlow.setOffsetX(0);
        primaryGlow.setOffsetY(0);

        
        DropShadow secondaryGlow = new DropShadow();
        secondaryGlow.setColor(neonColor.deriveColor(0, 0.80, 0.90, 0.30)); 
        secondaryGlow.setRadius(SECONDARY_GLOW_RADIUS);
        secondaryGlow.setSpread(SECONDARY_GLOW_SPREAD);
        secondaryGlow.setOffsetX(0);
        secondaryGlow.setOffsetY(0);
        secondaryGlow.setInput(primaryGlow);

        rectangle.setEffect(secondaryGlow);
    }

    
    public static void applyNeonGlow(Rectangle rectangle, Color baseColor) {
        if (rectangle == null || baseColor == null) {
            return;
        }

        Color neonColor = deriveNeonColor(baseColor);
        applyNeonGlow(rectangle, baseColor, neonColor);
    }

    
    public static void applyGhostNeon(Rectangle rectangle, Color baseColor, Color neonColor) {
        if (rectangle == null || baseColor == null || neonColor == null) {
            return;
        }
        
        
        rectangle.setFill(Color.web("#000000", 0.12));

        
        rectangle.setStroke(neonColor.deriveColor(0, 1.0, 1.0, 0.22));
        rectangle.setStrokeWidth(STROKE_WIDTH);
        rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

        
        rectangle.setArcHeight(ARC_RADIUS);
        rectangle.setArcWidth(ARC_RADIUS);

        
        DropShadow tiny = new DropShadow();
        tiny.setColor(neonColor.deriveColor(0, 0.85, 0.90, 0.08));
        tiny.setRadius(1.0);
        tiny.setSpread(0.06);
        tiny.setOffsetX(0);
        tiny.setOffsetY(0);
        rectangle.setEffect(tiny);
    }

    
    public static void applyPlacedStyle(Rectangle rectangle, Color baseColor, Color neonColor) {
        if (rectangle == null || baseColor == null || neonColor == null) return;

        rectangle.setFill(Color.web("#000000", 0.28));
        rectangle.setStroke(neonColor);
        rectangle.setStrokeWidth(1);
        rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        rectangle.setArcHeight(ARC_RADIUS);
        rectangle.setArcWidth(ARC_RADIUS);
        rectangle.setEffect(null);
    }

    
    public static void applyPlacedStyle(Rectangle rectangle, Color baseColor) {
        if (rectangle == null || baseColor == null) return;
        Color neonColor = deriveNeonColor(baseColor);
        applyPlacedStyle(rectangle, baseColor, neonColor);
    }

    
    private static Color deriveNeonColor(Color baseColor) {
        
        double hue = baseColor.getHue();
        double saturation = Math.min(1.0, baseColor.getSaturation() + 0.2);
        double brightness = Math.min(1.0, baseColor.getBrightness() + 0.25);
        return Color.hsb(hue, saturation, brightness);
    }
}


