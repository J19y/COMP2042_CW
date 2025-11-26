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

    private static final int STROKE_WIDTH = 1;
    private static final int PRIMARY_GLOW_RADIUS = 4;
    private static final double PRIMARY_GLOW_SPREAD = 0.18;
    private static final int SECONDARY_GLOW_RADIUS = 2;
    private static final double SECONDARY_GLOW_SPREAD = 0.25;
    private static final int ARC_RADIUS = 9;

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

        // Stroke: neon color - draw stroke inside the rectangle to avoid overflow
        rectangle.setStroke(neonColor);
        rectangle.setStrokeWidth(STROKE_WIDTH);
        rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

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
     * Applies a muted neon style intended for ghost previews.
     * The ghost keeps the same rounded corners and neon stroke hue, but with
     * reduced fill opacity and softened glow so it doesn't distract from the active brick.
     */
    public static void applyGhostNeon(Rectangle rectangle, Color baseColor, Color neonColor) {
        if (rectangle == null || baseColor == null || neonColor == null) {
            return;
        }
        // Make ghost follow the placed/inside-black visual language but lighter
        // Translucent inner fill keeps ghost subtle and consistent with merged bricks
        rectangle.setFill(Color.web("#000000", 0.12));

        // Softer neon stroke hint (drawn inside to avoid overflow)
        rectangle.setStroke(neonColor.deriveColor(0, 1.0, 1.0, 0.22));
        rectangle.setStrokeWidth(STROKE_WIDTH);
        rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

        // Keep rounded corners consistent with active and placed bricks
        rectangle.setArcHeight(ARC_RADIUS);
        rectangle.setArcWidth(ARC_RADIUS);

        // Ghost should be unobtrusive: no heavy glow, but a tiny softening effect is acceptable
        DropShadow tiny = new DropShadow();
        tiny.setColor(neonColor.deriveColor(0, 0.85, 0.90, 0.08));
        tiny.setRadius(1.0);
        tiny.setSpread(0.06);
        tiny.setOffsetX(0);
        tiny.setOffsetY(0);
        rectangle.setEffect(tiny);
    }

    /**
     * Applies the "inside black" style used for placed/merged bricks and now for falling bricks.
     * This keeps the same visual language as board cells: a black-ish inner fill and neon stroke,
     * with no heavy outer glow so bricks visually sit inside the container.
     */
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

    /**
     * Convenience overload: derive neon color from base and apply placed style.
     */
    public static void applyPlacedStyle(Rectangle rectangle, Color baseColor) {
        if (rectangle == null || baseColor == null) return;
        Color neonColor = deriveNeonColor(baseColor);
        applyPlacedStyle(rectangle, baseColor, neonColor);
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

