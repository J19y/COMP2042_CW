package com.comp2042.ui;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Registry for mapping integer cell values to Paint colors.
 * New brick IDs can register their colors here without modifying existing code.
 */
public final class ColorPalette {

    private ColorPalette() {}

    private static final Map<Integer, Paint> COLORS = new HashMap<>();

    static {
        // Default mappings (align with previous CellColor switch)
        COLORS.put(0, Color.TRANSPARENT);
        COLORS.put(1, Color.AQUA);
        COLORS.put(2, Color.BLUEVIOLET);
        COLORS.put(3, Color.DARKGREEN);
        COLORS.put(4, Color.YELLOW);
        COLORS.put(5, Color.RED);
        COLORS.put(6, Color.BEIGE);
        COLORS.put(7, Color.BURLYWOOD);
    }

    
    // Register or override the color for a cell id.
    public static void register(int id, Paint paint) {
        if (paint != null) {
            COLORS.put(id, paint);
        }
    }

    // Retrieve the color for a cell id, or null if none is registered.
    public static Paint get(int id) {
        return COLORS.get(id);
    }
}
