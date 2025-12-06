package com.comp2042.tetris.ui.theme;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Utility class for mapping cell values to colors.
 * Delegates to ColorPalette for brick colors and returns
 * transparent for empty cells. Provides fallback to white.
 *
 */
public final class CellColor {

    private CellColor() {}

    
    public static Paint fromValue(int value) {
    Paint p = ColorPalette.getInstance().getColor(value);
        if (p != null) {
            return p;
        }
        
        if (value == 0) return Color.TRANSPARENT;
        return Color.WHITE;
    }
}

