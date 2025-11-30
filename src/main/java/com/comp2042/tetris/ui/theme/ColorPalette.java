package com.comp2042.tetris.ui.theme;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Registry for mapping integer cell values to Paint colors.
 * New brick IDs can register their colors here without modifying existing code.
 * Now supports neon-glow styling for each brick type.
 */
public final class ColorPalette {

    private static final ColorPalette INSTANCE = new ColorPalette();

    private final Map<Integer, Paint> colors = new HashMap<>();
    private final Map<Integer, Color> neonColors = new HashMap<>();

    private ColorPalette() {
        // ID 0: Empty cell (transparent)
        registerInternal(0, Color.TRANSPARENT);
        registerNeonInternal(0, Color.TRANSPARENT);

        // Standard Tetris colors mapped to IDs 1-7 with refined neon colors
        // I-brick (Cyan) - Bright cyan neon
        registerInternal(1, Color.AQUA);
        registerNeonInternal(1, Color.web("#00FFFF"));

        // T-brick (Purple) - Rich purple neon
        registerInternal(2, Color.BLUEVIOLET);
        registerNeonInternal(2, Color.web("#BB00FF"));

        // L-brick (Orange) - Bright orange neon
        registerInternal(3, Color.DARKGREEN);
        registerNeonInternal(3, Color.web("#FF8800"));

        // J-brick (Yellow) - Bright yellow neon
        registerInternal(4, Color.YELLOW);
        registerNeonInternal(4, Color.web("#FFFF00"));

        // S-brick (Red) - Bright red neon
        registerInternal(5, Color.RED);
        registerNeonInternal(5, Color.web("#FF0000"));

        // Z-brick (Green) - Bright lime green neon
        registerInternal(6, Color.BEIGE);
        registerNeonInternal(6, Color.web("#00FF00"));

        // O-brick (Pink/Magenta) - Bright pink neon
        registerInternal(7, Color.BURLYWOOD);
        registerNeonInternal(7, Color.web("#FF00FF"));
    }

    public static ColorPalette getInstance() {
        return INSTANCE;
    }

    
    // Register or override the color for a cell id.
    public static void register(int id, Paint paint) {
        INSTANCE.registerInternal(id, paint);
    }

    private void registerInternal(int id, Paint paint) {
        if (paint != null) {
            colors.put(id, paint);
        }
    }

    public Paint getColor(int id) {
        return colors.get(id);
    }

    // Retrieve the color for a cell id, or null if none is registered.
    public static Paint get(int id) {
        return INSTANCE.getColor(id);
    }

    // Register or override the neon color for a cell id.
    public static void registerNeon(int id, Color neonColor) {
        INSTANCE.registerNeonInternal(id, neonColor);
    }

    private void registerNeonInternal(int id, Color neonColor) {
        if (neonColor != null) {
            neonColors.put(id, neonColor);
        }
    }

    public Color getNeonColor(int id) {
        return neonColors.get(id);
    }

    // Retrieve the neon color for a cell id, or null if none is registered.
    public static Color getNeon(int id) {
        return INSTANCE.getNeonColor(id);
    }
}
