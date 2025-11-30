package com.comp2042.tetris.ui.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Applies a retro neon flicker/CRT monitor effect to JavaFX nodes.
 * Creates a subtle opacity pulsing animation to mimic old neon signs or failing CRT monitors.
 */
public final class NeonFlickerEffect {

    private NeonFlickerEffect() {
        // Utility class, no instantiation
    }

    /**
     * Applies a subtle flickering animation to a node, cycling through opacity values.
     * 
     * @param node The JavaFX node to apply the flicker effect to
     * @param duration The total duration of the flicker cycle (e.g., Duration.seconds(2))
     * @param minOpacity Minimum opacity during flicker (0.0-1.0, typically 0.5-0.8)
     * @param maxOpacity Maximum opacity (should be 1.0 for full brightness)
     * @param cycleCount Number of times to repeat (-1 for infinite)
     */
    public static Timeline createFlickerAnimation(
            Node node,
            Duration duration,
            double minOpacity,
            double maxOpacity,
            int cycleCount) {
        
        Timeline timeline = new Timeline(
            // Ramp up to full brightness
            new KeyFrame(
                duration.multiply(0.1),
                new KeyValue(node.opacityProperty(), maxOpacity)
            ),
            // Hold briefly
            new KeyFrame(
                duration.multiply(0.3),
                new KeyValue(node.opacityProperty(), maxOpacity)
            ),
            // Quick dip (subtle flicker)
            new KeyFrame(
                duration.multiply(0.35),
                new KeyValue(node.opacityProperty(), minOpacity)
            ),
            // Recover instantly
            new KeyFrame(
                duration.multiply(0.4),
                new KeyValue(node.opacityProperty(), maxOpacity)
            ),
            // Hold at max for bulk of cycle
            new KeyFrame(
                duration.multiply(0.9),
                new KeyValue(node.opacityProperty(), maxOpacity)
            ),
            // Fade to min at end of cycle
            new KeyFrame(
                duration,
                new KeyValue(node.opacityProperty(), minOpacity)
            )
        );
        
        timeline.setCycleCount(cycleCount);
        return timeline;
    }

    /**
     * Creates a standard retro neon flicker with moderate settings.
     * 
     * @param node The node to animate
     * @return A Timeline configured for continuous flickering (infinite cycles)
     */
    public static Timeline createStandardFlicker(Node node) {
        return createFlickerAnimation(
            node,
            Duration.seconds(3.0),  // 3-second cycle
            0.65,                    // Min opacity (65%)
            1.0,                     // Max opacity (100%)
            Timeline.INDEFINITE      // Infinite repetition
        );
    }

    /**
     * Creates a fast, more pronounced flicker for high-intensity effects.
     * 
     * @param node The node to animate
     * @return A Timeline configured for fast flickering
     */
    public static Timeline createIntenseFlicker(Node node) {
        return createFlickerAnimation(
            node,
            Duration.seconds(1.5),
            0.5,
            1.0,
            Timeline.INDEFINITE
        );
    }

    /**
     * Creates a slow, subtle flicker for background elements.
     * 
     * @param node The node to animate
     * @return A Timeline configured for slow, subtle flickering
     */
    public static Timeline createSubtleFlicker(Node node) {
        return createFlickerAnimation(
            node,
            Duration.seconds(4.5),
            0.8,
            1.0,
            Timeline.INDEFINITE
        );
    }
}
