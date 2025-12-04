package com.comp2042.tetris.ui.animation;

import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;


public final class NeonFlickerEffectExamples {

    
    public static void example1_SubtleFlickerOnContainer(VBox container) {
        
        
        Timeline flicker = NeonFlickerEffect.createSubtleFlicker(container);
        flicker.play();
    }

    
    public static void example2_StandardFlickerOnText(Text textElement) {
        
        
        Timeline flicker = NeonFlickerEffect.createStandardFlicker(textElement);
        flicker.play();
    }

    
    public static void example3_IntenseFlickerForAttention(Node importantUI) {
        
        
        Timeline flicker = NeonFlickerEffect.createIntenseFlicker(importantUI);
        flicker.play();
    }

    
    public static void example4_CustomFlicker(Node node) {
        
        Timeline flicker = NeonFlickerEffect.createFlickerAnimation(
            node,
            Duration.seconds(2.0),      
            0.5,                        
            1.0,                        
            Timeline.INDEFINITE         
        );
        flicker.play();
    }

    
    public static void example5_FiniteFlicker(Node node) {
        
        Timeline flicker = NeonFlickerEffect.createFlickerAnimation(
            node,
            Duration.seconds(3.0),
            0.65,
            1.0,
            5  
        );
        flicker.play();
    }

    
    public static void example6_MultipleNodesWithDifferentEffects(
            Text title, VBox controls, Node highlight) {
        
        
        
        NeonFlickerEffect.createSubtleFlicker(title).play();
        
        
        NeonFlickerEffect.createStandardFlicker(controls).play();
        
        
        NeonFlickerEffect.createIntenseFlicker(highlight).play();
    }

    
    public static void example7_StopFlickerAnimation(Node node) {
        
        Timeline flicker = NeonFlickerEffect.createSubtleFlicker(node);
        flicker.play();
        
        
        flicker.stop();
        
        
        node.setOpacity(1.0);
    }

    
    public static class MenuControlsGuideExample {
        private javafx.scene.layout.HBox controlsGuideContainer;
        private javafx.scene.layout.VBox controlsTextBox;
        
        
        public void setupControlsGuide() {
            if (controlsTextBox == null) return;
            
            
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
            
            
            if (controlsGuideContainer != null) {
                
                
                NeonFlickerEffect
                    .createSubtleFlicker(controlsGuideContainer)
                    .play();
            }
        }
    }

    
    public static class DynamicFlickerExample {
        private Node warningIndicator;
        private Timeline currentFlicker;
        
        
        public void updateFlickerForGameState(String gameState) {
            
            if (currentFlicker != null) {
                currentFlicker.stop();
            }
            
            
            switch (gameState) {
                case "MENU":
                    
                    currentFlicker = NeonFlickerEffect.createSubtleFlicker(warningIndicator);
                    break;
                case "PLAYING":
                    
                    currentFlicker = NeonFlickerEffect.createStandardFlicker(warningIndicator);
                    break;
                case "CRITICAL":
                    
                    currentFlicker = NeonFlickerEffect.createIntenseFlicker(warningIndicator);
                    break;
                default:
                    
                    warningIndicator.setOpacity(1.0);
                    return;
            }
            
            currentFlicker.play();
        }
    }

    
    public static void example10_SequentialFlickers(
            Node first, Node second, Node third) {
        
        Timeline flicker1 = NeonFlickerEffect.createFlickerAnimation(
            first, Duration.seconds(2), 0.6, 1.0, 3
        );
        
        
        flicker1.setOnFinished(e -> {
            Timeline flicker2 = NeonFlickerEffect.createFlickerAnimation(
                second, Duration.seconds(2), 0.6, 1.0, 3
            );
            flicker2.setOnFinished(e2 -> {
                
                NeonFlickerEffect.createFlickerAnimation(
                    third, Duration.seconds(2), 0.6, 1.0, 3
                ).play();
            });
            flicker2.play();
        });
        
        flicker1.play();
    }

    
    
    
    
    
    
    

}

