package com.comp2042.ui;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Maps board cell integer values to UI colors via a registry (ColorPalette).
 * New mappings can be registered without modifying this class (OCP).
 */
public final class CellColor {

    private CellColor() {}

    /**
     * Returns the paint to use for a given cell value.
     * Defaults to transparent for 0 and white for unknown ids.
     */
    public static Paint fromValue(int value) {
        Paint p = ColorPalette.get(value);
        if (p != null) {
            return p;
        }
        // Maintain prior behavior for empty cells and unknown ids
        if (value == 0) return Color.TRANSPARENT;
        return Color.WHITE;
    }
}
