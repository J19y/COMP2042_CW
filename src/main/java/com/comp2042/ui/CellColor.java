package com.comp2042.ui;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Maps board cell integer values to UI colors.
 * Extracted from GuiController to follow SRP.
 */
public final class CellColor {

    private CellColor() {}

    /**
     * Returns the paint to use for a given cell value.
     * 0 means empty (transparent); other values map to distinct colors.
     */
    public static Paint fromValue(int value) {
        return switch (value) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.AQUA;
            case 2 -> Color.BLUEVIOLET;
            case 3 -> Color.DARKGREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.BEIGE;
            case 7 -> Color.BURLYWOOD;
            default -> Color.WHITE;
        };
    }
}
