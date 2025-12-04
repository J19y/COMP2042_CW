package com.comp2042.tetris.ui.theme;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


public final class ColorPalette {

    private static final ColorPalette INSTANCE = new ColorPalette();

    private final Map<Integer, Paint> colors = new HashMap<>();
    private final Map<Integer, Color> neonColors = new HashMap<>();

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

    public static ColorPalette getInstance() {
        return INSTANCE;
    }

    
    
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

    
    public static Paint get(int id) {
        return INSTANCE.getColor(id);
    }

    
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

    
    public static Color getNeon(int id) {
        return INSTANCE.getNeonColor(id);
    }
}

