package com.comp2042.tetris.ui.theme;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Applies professional neon-glow styling to Tetris bricks.
 * Each brick features:
 * - Black inner fill (empty/void appearance)
 * - Refined neon stroke (2px outline)
 * - Refined outer glow (professional appearance)
 * - Subtle rounded corners
 */
public final class NeonGlowStyle {

    private static final int STROKE_WIDTH = 2;
    private static final int PRIMARY_GLOW_RADIUS = 6;
    private static final double PRIMARY_GLOW_SPREAD = 0.2;
    private static final int SECONDARY_GLOW_RADIUS = 3;
    private static final double SECONDARY_GLOW_SPREAD = 0.3;
    private static final int ARC_RADIUS = 2;

    private NeonGlowStyle() {}

    /**
     * Applies professional neon-glow styling to a rectangle.
     * @param rectangle the Rectangle to style
     * @param baseColor the base color of the brick
     * @param neonColor the bright neon color for the stroke
     */
    public static void applyNeonGlow(Rectangle rectangle, Color baseColor, Color neonColor) {
        if (rectangle == null || baseColor == null || neonColor == null) {
            return;
        }

        // Inner fill: dark semi-transparent (void appearance with slight visibility)
        rectangle.setFill(Color.web("#000000", 0.3));

        // Stroke: neon color (2px width) - refined outline
        rectangle.setStroke(neonColor);
        rectangle.setStrokeWidth(STROKE_WIDTH);

        // Subtle rounded corners
        rectangle.setArcHeight(ARC_RADIUS);
        rectangle.setArcWidth(ARC_RADIUS);

        // Create refined glow effect for professional neon look
        DropShadow primaryGlow = new DropShadow();
        primaryGlow.setColor(neonColor.deriveColor(0, 0.85, 0.80, 0.55)); // Controlled opacity and saturation
        primaryGlow.setRadius(PRIMARY_GLOW_RADIUS);
        primaryGlow.setSpread(PRIMARY_GLOW_SPREAD);
        primaryGlow.setOffsetX(0);
        primaryGlow.setOffsetY(0);

        // Add subtle secondary glow layer for refined brightness
        DropShadow secondaryGlow = new DropShadow();
        secondaryGlow.setColor(neonColor.deriveColor(0, 0.80, 0.90, 0.30)); // More transparent secondary
        secondaryGlow.setRadius(SECONDARY_GLOW_RADIUS);
        secondaryGlow.setSpread(SECONDARY_GLOW_SPREAD);
        secondaryGlow.setOffsetX(0);
        secondaryGlow.setOffsetY(0);
        secondaryGlow.setInput(primaryGlow);

        rectangle.setEffect(secondaryGlow);
    }

    /**
     * Applies neon-glow styling with automatic neon color derivation.
     * The neon color is a brighter, more saturated version of the base color.
     * @param rectangle the Rectangle to style
     * @param baseColor the base color of the brick
     */
    public static void applyNeonGlow(Rectangle rectangle, Color baseColor) {
        if (rectangle == null || baseColor == null) {
            return;
        }

        Color neonColor = deriveNeonColor(baseColor);
        applyNeonGlow(rectangle, baseColor, neonColor);
    }

    /**
     * Derives a bright neon color from a base color.
     * Creates deeper, richer neon colors with good contrast.
     * @param baseColor the base color
     * @return a refined neon version with controlled brightness
     */
    private static Color deriveNeonColor(Color baseColor) {
        // Convert to HSB and refine for neon aesthetic
        double hue = baseColor.getHue();
        double saturation = Math.min(1.0, baseColor.getSaturation() + 0.2);
        double brightness = Math.min(1.0, baseColor.getBrightness() + 0.25);
        return Color.hsb(hue, saturation, brightness);
    }
}

