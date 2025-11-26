package com.comp2042.tetris.ui.animation;

import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * USAGE EXAMPLES for NeonFlickerEffect Animation Utility
 * 
 * This file demonstrates how to use the NeonFlickerEffect class
 * to create retro neon and CRT monitor effects on JavaFX nodes.
 */
public final class NeonFlickerEffectExamples {

    // Example 1: Apply subtle flicker to a container (like our Controls Guide)
    public static void example1_SubtleFlickerOnContainer(VBox container) {
        // Perfect for background UI elements that should be visible
        // Opacity oscillates between 80% and 100% over 4.5 seconds
        Timeline flicker = NeonFlickerEffect.createSubtleFlicker(container);
        flicker.play();
    }

    // Example 2: Apply standard flicker to a text element
    public static void example2_StandardFlickerOnText(Text textElement) {
        // Balanced effect - noticeable but not distracting
        // Opacity oscillates between 65% and 100% over 3 seconds
        Timeline flicker = NeonFlickerEffect.createStandardFlicker(textElement);
        flicker.play();
    }

    // Example 3: Apply intense flicker to highlight something important
    public static void example3_IntenseFlickerForAttention(Node importantUI) {
        // Fast, pronounced effect - grabs attention
        // Opacity oscillates between 50% and 100% over 1.5 seconds
        Timeline flicker = NeonFlickerEffect.createIntenseFlicker(importantUI);
        flicker.play();
    }

    // Example 4: Custom flicker with specific parameters
    public static void example4_CustomFlicker(Node node) {
        // Create a completely custom flicker with your own parameters:
        Timeline flicker = NeonFlickerEffect.createFlickerAnimation(
            node,
            Duration.seconds(2.0),      // 2-second cycle
            0.5,                        // Min opacity: 50% (very dim)
            1.0,                        // Max opacity: 100% (full brightness)
            Timeline.INDEFINITE         // Repeat forever
        );
        flicker.play();
    }

    // Example 5: Flicker that stops after a certain number of cycles
    public static void example5_FiniteFlicker(Node node) {
        // Flicker only 5 times, then stop
        Timeline flicker = NeonFlickerEffect.createFlickerAnimation(
            node,
            Duration.seconds(3.0),
            0.65,
            1.0,
            5  // Cycle count: 5 repetitions only
        );
        flicker.play();
    }

    // Example 6: Multiple nodes with different flicker effects
    public static void example6_MultipleNodesWithDifferentEffects(
            Text title, VBox controls, Node highlight) {
        // Each element gets its own flicker pattern
        
        // Title: subtle (stays visible)
        NeonFlickerEffect.createSubtleFlicker(title).play();
        
        // Controls guide: standard (balanced)
        NeonFlickerEffect.createStandardFlicker(controls).play();
        
        // Important alert: intense (very noticeable)
        NeonFlickerEffect.createIntenseFlicker(highlight).play();
    }

    // Example 7: Stop a flicker animation
    public static void example7_StopFlickerAnimation(Node node) {
        // Create animation
        Timeline flicker = NeonFlickerEffect.createSubtleFlicker(node);
        flicker.play();
        
        // ... later, stop the animation
        flicker.stop();
        
        // Optional: Reset opacity to fully visible
        node.setOpacity(1.0);
    }

    // Example 8: Real-world usage in MenuController
    public static class MenuControlsGuideExample {
        private javafx.scene.layout.HBox controlsGuideContainer;
        private javafx.scene.layout.VBox controlsTextBox;
        
        // Setup method that populates and animates the controls guide
        public void setupControlsGuide() {
            if (controlsTextBox == null) return;
            
            // Build the controls list programmatically
            controlsTextBox.getChildren().clear();
            
            Text titleText = new Text("WASD / Mouse");
            titleText.getStyleClass().add("controls-guide-title");
            controlsTextBox.getChildren().add(titleText);
            
            String[][] controls = {
                {"W/↑", "Move Up"},
                {"A/←", "Move Left"},
                {"S/↓", "Move Down"},
                {"D/→", "Move Right"},
                {"Space", "Hard Drop"},
                {"P", "Pause"},
                {"Mouse", "Alternative Movement"}
            };
            
            for (String[] control : controls) {
                javafx.scene.layout.HBox row = new javafx.scene.layout.HBox(6);
                row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                
                Text keyText = new Text(control[0]);
                keyText.getStyleClass().add("neon-key");
                
                Text descText = new Text(control[1]);
                descText.getStyleClass().add("controls-guide-subtitle");
                
                row.getChildren().addAll(keyText, descText);
                controlsTextBox.getChildren().add(row);
            }
            
            // Apply subtle flicker animation to entire controls guide
            if (controlsGuideContainer != null) {
                // This creates a smooth 4.5-second cycle with 80% minimum opacity
                // Perfect for background UI elements
                NeonFlickerEffect
                    .createSubtleFlicker(controlsGuideContainer)
                    .play();
            }
        }
    }

    // Example 9: Dynamic flicker based on game state
    public static class DynamicFlickerExample {
        private Node warningIndicator;
        private Timeline currentFlicker;
        
        // Switch between different flicker intensities based on state
        public void updateFlickerForGameState(String gameState) {
            // Stop current animation
            if (currentFlicker != null) {
                currentFlicker.stop();
            }
            
            // Apply new animation based on state
            switch (gameState) {
                case "MENU":
                    // Calm, subtle flicker
                    currentFlicker = NeonFlickerEffect.createSubtleFlicker(warningIndicator);
                    break;
                case "PLAYING":
                    // Normal, balanced flicker
                    currentFlicker = NeonFlickerEffect.createStandardFlicker(warningIndicator);
                    break;
                case "CRITICAL":
                    // Fast, intense flicker - grab attention!
                    currentFlicker = NeonFlickerEffect.createIntenseFlicker(warningIndicator);
                    break;
                default:
                    // No animation
                    warningIndicator.setOpacity(1.0);
                    return;
            }
            
            currentFlicker.play();
        }
    }

    // Example 10: Sequence multiple flickers in order
    public static void example10_SequentialFlickers(
            Node first, Node second, Node third) {
        
        Timeline flicker1 = NeonFlickerEffect.createFlickerAnimation(
            first, Duration.seconds(2), 0.6, 1.0, 3
        );
        
        // Play second flicker after first finishes
        flicker1.setOnFinished(e -> {
            Timeline flicker2 = NeonFlickerEffect.createFlickerAnimation(
                second, Duration.seconds(2), 0.6, 1.0, 3
            );
            flicker2.setOnFinished(e2 -> {
                // Play third after second
                NeonFlickerEffect.createFlickerAnimation(
                    third, Duration.seconds(2), 0.6, 1.0, 3
                ).play();
            });
            flicker2.play();
        });
        
        flicker1.play();
    }

    // ===== ANIMATION PATTERN GUIDE =====
    
    /*
     * OPACITY CYCLE BREAKDOWN (for createFlickerAnimation):
     * 
     * Time  0% ─────────────────┐ (Ramp up to full brightness)
     *                          │
     * Time 10% ─────────────────┤ MAX (100%)
     *         │                │
     * Time 30% ─────────────────┤ (Hold briefly at max)
     *         │                │
     * Time 35% ─┬───────────────┘ (Quick dip - the "flicker")
     *         │ │
     * Time 40% ─┴─ MIN (var: 0.5-0.8)
     *           (Recover instantly)
     *
     * Time 40-90% ──────────────── MAX (Hold for bulk of cycle)
     *
     * Time 90-100% ───→ MIN (Fade down to end of cycle)
     * 
     * Then repeat or end based on cycleCount
     */
    
    // ===== COLOR/EFFECT COMBINATIONS FOR MENU.CSS =====
    
    /*
     * CYAN (Holographic)          AMBER (Keyboard)        MAGENTA (Critical)
     * ─────────────────          ─────────────────        ──────────────────
     * Color: #00ffdd             Color: #ffaa00           Color: #ff00ff
     * Glow:  rgba(0,255,221)     Glow:  rgba(255,170,0)  Glow:  rgba(255,0,255)
     * Radius: 6-8px              Radius: 5-7px            Radius: 10-12px
     * Opacity: 0.5-0.8           Opacity: 0.6-0.8         Opacity: 0.7-0.9
     * 
     * Example CSS combination:
     * -fx-fill: #00ffdd;
     * -fx-effect: dropshadow(gaussian, rgba(0, 255, 221, 0.7), 8, 0.5, 0, 0);
     */

}
