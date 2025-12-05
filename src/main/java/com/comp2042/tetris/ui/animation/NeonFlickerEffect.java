package com.comp2042.tetris.ui.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Utility class for creating neon flicker animation effects on JavaFX nodes.
 * <p>
 * Provides methods to create various intensity flicker animations
 * that simulate a neon light effect by varying node opacity.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public final class NeonFlickerEffect {

    /**
     * Private constructor to prevent instantiation.
     */
    private NeonFlickerEffect() {
        
    }

    /**
     * Creates a customizable flicker animation for a node.
     *
     * @param node the JavaFX node to animate
     * @param duration the total duration of one flicker cycle
     * @param minOpacity the minimum opacity during flicker
     * @param maxOpacity the maximum opacity during flicker
     * @param cycleCount the number of cycles (-1 for infinite)
     * @return the configured Timeline animation
     */
    public static Timeline createFlickerAnimation(
            Node node,
            Duration duration,
            double minOpacity,
            double maxOpacity,
            int cycleCount) {
        
        Timeline timeline = new Timeline(
            
            new KeyFrame(
                duration.multiply(0.1),
                new KeyValue(node.opacityProperty(), maxOpacity)
            ),
            
            new KeyFrame(
                duration.multiply(0.3),
                new KeyValue(node.opacityProperty(), maxOpacity)
            ),
            
            new KeyFrame(
                duration.multiply(0.35),
                new KeyValue(node.opacityProperty(), minOpacity)
            ),
            
            new KeyFrame(
                duration.multiply(0.4),
                new KeyValue(node.opacityProperty(), maxOpacity)
            ),
            
            new KeyFrame(
                duration.multiply(0.9),
                new KeyValue(node.opacityProperty(), maxOpacity)
            ),
            
            new KeyFrame(
                duration,
                new KeyValue(node.opacityProperty(), minOpacity)
            )
        );
        
        timeline.setCycleCount(cycleCount);
        return timeline;
    }

    /**
     * Creates a standard neon flicker effect.
     * <p>
     * Uses a 3-second cycle with opacity range 0.65-1.0.
     * </p>
     *
     * @param node the node to animate
     * @return the configured Timeline animation
     */
    public static Timeline createStandardFlicker(Node node) {
        return createFlickerAnimation(
            node,
            Duration.seconds(3.0),  
            0.65,                    
            1.0,                     
            Timeline.INDEFINITE      
        );
    }

    /**
     * Creates an intense neon flicker effect.
     * <p>
     * Uses a 1.5-second cycle with opacity range 0.5-1.0 for attention-grabbing animations.
     * </p>
     *
     * @param node the node to animate
     * @return the configured Timeline animation
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
     * Creates a subtle neon flicker effect.
     * <p>
     * Uses a 4.5-second cycle with opacity range 0.8-1.0 for gentle background animations.
     * </p>
     *
     * @param node the node to animate
     * @return the configured Timeline animation
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

