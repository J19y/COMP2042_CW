package com.comp2042.tetris.ui.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;


public final class NeonFlickerEffect {

    private NeonFlickerEffect() {
        
    }

    
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

    
    public static Timeline createStandardFlicker(Node node) {
        return createFlickerAnimation(
            node,
            Duration.seconds(3.0),  
            0.65,                    
            1.0,                     
            Timeline.INDEFINITE      
        );
    }

    
    public static Timeline createIntenseFlicker(Node node) {
        return createFlickerAnimation(
            node,
            Duration.seconds(1.5),
            0.5,
            1.0,
            Timeline.INDEFINITE
        );
    }

    
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

