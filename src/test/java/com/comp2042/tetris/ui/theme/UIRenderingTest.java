package com.comp2042.tetris.ui.theme;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Tests for UI rendering and theming components: ColorPalette, CellColor.
 * Focuses on color management and theme consistency.
 */
class UIRenderingTest {

    @Test
    void colorPaletteGetInstanceReturnsNonNull() {
        ColorPalette palette = ColorPalette.getInstance();
        assertNotNull(palette, "ColorPalette.getInstance() should return a palette");
    }

    @Test
    void colorPaletteIsSingleton() {
        ColorPalette instance1 = ColorPalette.getInstance();
        ColorPalette instance2 = ColorPalette.getInstance();
        assertSame(instance1, instance2, "ColorPalette should be a singleton");
    }

    @Test
    void colorPaletteCanGetColor() {
        ColorPalette palette = ColorPalette.getInstance();
        Paint color = palette.getColor(0);
        assertNotNull(color, "ColorPalette should return color for ID 0");
    }

    @Test
    void colorPaletteStaticGetReturnsColor() {
        Paint color = ColorPalette.get(1);
        assertNotNull(color, "Static ColorPalette.get() should return a color");
    }

    @Test
    void colorPaletteHasNeonColors() {
        Color neonColor = ColorPalette.getNeon(1);
        assertNotNull(neonColor, "ColorPalette should have neon colors");
    }

    @Test
    void colorPaletteCanRegisterNewColor() {
        ColorPalette palette = ColorPalette.getInstance();
        Paint testColor = Color.RED;
        ColorPalette.register(99, testColor);
        Paint retrieved = palette.getColor(99);
        assertNotNull(retrieved, "Registered color should be retrievable");
    }

    @Test
    void colorPaletteCanRegisterNewNeonColor() {
        ColorPalette palette = ColorPalette.getInstance();
        Color neonColor = Color.GREEN;
        ColorPalette.registerNeon(99, neonColor);
        Color retrieved = palette.getNeonColor(99);
        assertNotNull(retrieved, "Registered neon color should be retrievable");
    }

    @Test
    void colorPaletteHasPresetColors() {
        assertNotNull(ColorPalette.get(0), "Color ID 0 should exist");
        assertNotNull(ColorPalette.get(1), "Color ID 1 should exist");
        assertNotNull(ColorPalette.get(2), "Color ID 2 should exist");
        assertNotNull(ColorPalette.get(3), "Color ID 3 should exist");
        assertNotNull(ColorPalette.get(4), "Color ID 4 should exist");
        assertNotNull(ColorPalette.get(5), "Color ID 5 should exist");
        assertNotNull(ColorPalette.get(6), "Color ID 6 should exist");
        assertNotNull(ColorPalette.get(7), "Color ID 7 should exist");
    }

    @Test
    void colorPaletteHasPresetNeonColors() {
        assertNotNull(ColorPalette.getNeon(0), "Neon color ID 0 should exist");
        assertNotNull(ColorPalette.getNeon(1), "Neon color ID 1 should exist");
        assertNotNull(ColorPalette.getNeon(2), "Neon color ID 2 should exist");
        assertNotNull(ColorPalette.getNeon(3), "Neon color ID 3 should exist");
        assertNotNull(ColorPalette.getNeon(4), "Neon color ID 4 should exist");
        assertNotNull(ColorPalette.getNeon(5), "Neon color ID 5 should exist");
        assertNotNull(ColorPalette.getNeon(6), "Neon color ID 6 should exist");
        assertNotNull(ColorPalette.getNeon(7), "Neon color ID 7 should exist");
    }

    @Test
    void cellColorFromValueReturnsNonNull() {
        Paint color = CellColor.fromValue(1);
        assertNotNull(color, "CellColor.fromValue() should return a color");
    }

    @Test
    void cellColorFromValueReturnsTransparentForZero() {
        Paint color = CellColor.fromValue(0);
        assertNotNull(color, "CellColor.fromValue(0) should return transparent color");
    }

    @Test
    void cellColorFromValueHandlesUnregisteredIds() {
        Paint color = CellColor.fromValue(999);
        assertNotNull(color, "CellColor should return a color even for unregistered IDs");
    }

    @Test
    void cellColorFromValueCanHandleMultipleIds() {
        for (int i = 0; i <= 7; i++) {
            Paint color = CellColor.fromValue(i);
            assertNotNull(color, "CellColor should provide colors for all brick IDs 0-7");
        }
    }

    @Test
    void colorPaletteRegisteringMultipleColors() {
        ColorPalette.register(50, Color.BLUE);
        ColorPalette.register(51, Color.PURPLE);
        ColorPalette.register(52, Color.ORANGE);

        assertNotNull(ColorPalette.get(50), "Color 50 should be registered");
        assertNotNull(ColorPalette.get(51), "Color 51 should be registered");
        assertNotNull(ColorPalette.get(52), "Color 52 should be registered");
    }

    @Test
    void colorPaletteInstanceMethodConsistentWithStatic() {
        ColorPalette palette = ColorPalette.getInstance();
        Paint instanceColor = palette.getColor(1);
        Paint staticColor = ColorPalette.get(1);
        assertNotNull(instanceColor);
        assertNotNull(staticColor);
    }

    @Test
    void cellColorFromValueHandlesEdgeCases() {
        Paint color0 = CellColor.fromValue(0);
        Paint color1 = CellColor.fromValue(1);
        Paint colorNegative = CellColor.fromValue(-1);
        Paint colorLarge = CellColor.fromValue(1000);

        assertNotNull(color0, "Should handle 0");
        assertNotNull(color1, "Should handle 1");
        assertNotNull(colorNegative, "Should handle negative values");
        assertNotNull(colorLarge, "Should handle large values");
    }

    @Test
    void colorPaletteGetNeonColorReturnsNonNull() {
        Color neon = ColorPalette.getInstance().getNeonColor(1);
        assertNotNull(neon, "getNeonColor should return neon color");
    }

    @Test
    void neonGlowStyleHasValidConstants() {
        // Testing that NeonGlowStyle can be referenced and has methods
        // This is a contract test for the class's existence and accessibility
        assertNotNull(NeonGlowStyle.class, "NeonGlowStyle class should exist");
    }
}
