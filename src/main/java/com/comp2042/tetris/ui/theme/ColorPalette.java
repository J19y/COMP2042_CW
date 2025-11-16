package com.comp2042.tetris.ui.theme;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Registry for mapping integer cell values to Paint colors.
 * New brick IDs can register their colors here without modifying existing code.
 */
public final class ColorPalette {

    private static final ColorPalette INSTANCE = new ColorPalette();

    private final Map<Integer, Paint> colors = new HashMap<>();

    private ColorPalette() {
        registerInternal(0, Color.TRANSPARENT);
        registerInternal(1, Color.AQUA);
        registerInternal(2, Color.BLUEVIOLET);
        registerInternal(3, Color.DARKGREEN);
        registerInternal(4, Color.YELLOW);
        registerInternal(5, Color.RED);
        registerInternal(6, Color.BEIGE);
        registerInternal(7, Color.BURLYWOOD);
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
}
