package com.comp2042.tetris.ui.theme;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Singleton color palette manager for brick and UI colors.
 * <p>
 * Maintains mappings from brick IDs to both standard and neon colors.
 * Supports custom color registration for extensibility.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public final class ColorPalette {

    private static final ColorPalette INSTANCE = new ColorPalette();

    private final Map<Integer, Paint> colors = new HashMap<>();
    private final Map<Integer, Color> neonColors = new HashMap<>();

    /**
     * Private constructor initializes default brick colors.
     */
    private ColorPalette() {
        
        registerInternal(0, Color.TRANSPARENT);
        registerNeonInternal(0, Color.TRANSPARENT);

        
        
        registerInternal(1, Color.AQUA);
        registerNeonInternal(1, Color.web("#00FFFF"));

        
        registerInternal(2, Color.BLUEVIOLET);
        registerNeonInternal(2, Color.web("#BB00FF"));

        
        registerInternal(3, Color.DARKGREEN);
        registerNeonInternal(3, Color.web("#FF8800"));

        
        registerInternal(4, Color.YELLOW);
        registerNeonInternal(4, Color.web("#FFFF00"));

        
        registerInternal(5, Color.RED);
        registerNeonInternal(5, Color.web("#FF0000"));

        
        registerInternal(6, Color.BEIGE);
        registerNeonInternal(6, Color.web("#00FF00"));

        
        registerInternal(7, Color.BURLYWOOD);
        registerNeonInternal(7, Color.web("#FF00FF"));
    }

    /**
     * Returns the singleton instance.
     *
     * @return the ColorPalette instance
     */
    public static ColorPalette getInstance() {
        return INSTANCE;
    }

    /**
     * Registers a custom color for a brick ID.
     *
     * @param id the brick ID
     * @param paint the color to associate
     */
    public static void register(int id, Paint paint) {
        INSTANCE.registerInternal(id, paint);
    }

    private void registerInternal(int id, Paint paint) {
        if (paint != null) {
            colors.put(id, paint);
        }
    }

    /**
     * Gets the color for a brick ID.
     *
     * @param id the brick ID
     * @return the associated color
     */
    public Paint getColor(int id) {
        return colors.get(id);
    }

    /**
     * Static convenience method to get a color.
     *
     * @param id the brick ID
     * @return the associated color
     */
    public static Paint get(int id) {
        return INSTANCE.getColor(id);
    }

    /**
     * Registers a neon color for a brick ID.
     *
     * @param id the brick ID
     * @param neonColor the neon color to associate
     */
    public static void registerNeon(int id, Color neonColor) {
        INSTANCE.registerNeonInternal(id, neonColor);
    }

    private void registerNeonInternal(int id, Color neonColor) {
        if (neonColor != null) {
            neonColors.put(id, neonColor);
        }
    }

    /**
     * Gets the neon color for a brick ID.
     *
     * @param id the brick ID
     * @return the associated neon color
     */
    public Color getNeonColor(int id) {
        return neonColors.get(id);
    }

    /**
     * Static convenience method to get a neon color.
     *
     * @param id the brick ID
     * @return the associated neon color
     */
    public static Color getNeon(int id) {
        return INSTANCE.getNeonColor(id);
    }
}

