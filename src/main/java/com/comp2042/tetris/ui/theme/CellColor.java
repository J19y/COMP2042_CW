package com.comp2042.tetris.ui.theme;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


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

