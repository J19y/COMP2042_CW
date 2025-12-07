package com.comp2042.tetris.ui.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Manages the main menu title setup and animations.
 * <p>
 * Creates the TETRIS title with colorful neon-styled letters
 * and flicker animations for each character.
 * </p>
 *
 * @version 1.0
 */
public class TitleSetupManager {
    private final HBox titleContainer;
    private final Text yearText;
    private final StackPane rootPane;
    private final List<Timeline> titleFlickers = new ArrayList<>();

    private static final String TITLE = "TETRIS";
    private static final Color[] TITLE_COLORS = {
            Color.BLUE, Color.RED, Color.YELLOW, Color.LIME, Color.MAGENTA, Color.ORANGE
    };

    public TitleSetupManager(HBox titleContainer, Text yearText, StackPane rootPane) {
        this.titleContainer = titleContainer;
        this.yearText = yearText;
        this.rootPane = rootPane;
    }

    
    public void setupTitle() {
        Font customFont = loadTitleFont();

        
        for (int i = 0; i < TITLE.length(); i++) {
            Text letter = new Text(String.valueOf(TITLE.charAt(i)));
            letter.getStyleClass().add("title-text");
            letter.setFont(customFont);
            letter.setFill(TITLE_COLORS[i % TITLE_COLORS.length]);

            try {
                String family = customFont.getName();
                letter.setStyle("-fx-font-size: 130px; -fx-font-family: '" + family + "';");
            } catch (Exception ignored) {
                letter.setStyle("-fx-font-size: 130px;");
            }

            
            DropShadow glow = new DropShadow();
            glow.setColor(TITLE_COLORS[i % TITLE_COLORS.length]);
            glow.setRadius(16);
            glow.setSpread(0.25);
            letter.setEffect(glow);

            titleContainer.getChildren().add(letter);
        }

        
        setupLetterAnimations();

        titleContainer.setSpacing(3);
        titleContainer.setTranslateY(-12);

        
        setupYearText(customFont);
    }

    
    private Font loadTitleFont() {
        String[] fontPaths = {
                "/fonts/AXR ArcadeMachine.ttf",
                "/fonts/Arcadia-SVG.ttf",
                "/Retro Pixel.otf",
                "/fonts/PressStart2P-vaV7.ttf",
                "/fonts/digital.ttf"
        };

        int[] fontSizes = {130, 130, 130, 130, 80};

        for (int i = 0; i < fontPaths.length; i++) {
            try {
                Font font = Font.loadFont(getClass().getResourceAsStream(fontPaths[i]), fontSizes[i]);
                if (font != null) {
                    System.out.println("Loaded custom font: " + font.getName());
                    return font;
                }
            } catch (Exception e) {
                System.err.println("Failed to load " + fontPaths[i] + ": " + e.getMessage());
            }
        }

        
        System.err.println("Using fallback font: Impact");
        return Font.font("Impact", 90);
    }

    
    private void setupLetterAnimations() {
        for (Node node : titleContainer.getChildren()) {
            if (node instanceof Text letter) {
                Timeline flicker = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(((DropShadow) letter.getEffect()).radiusProperty(), 10.0)),
                        new KeyFrame(Duration.millis(100), new KeyValue(((DropShadow) letter.getEffect()).radiusProperty(), 20.0)),
                        new KeyFrame(Duration.millis(200), new KeyValue(((DropShadow) letter.getEffect()).radiusProperty(), 10.0)),
                        new KeyFrame(Duration.millis(300), new KeyValue(((DropShadow) letter.getEffect()).radiusProperty(), 15.0)),
                        new KeyFrame(Duration.millis(400), new KeyValue(((DropShadow) letter.getEffect()).radiusProperty(), 10.0))
                );
                flicker.setCycleCount(Timeline.INDEFINITE);
                titleFlickers.add(flicker);

                letter.setOnMouseEntered(e -> flicker.play());
                letter.setOnMouseExited(e -> {
                    flicker.stop();
                    ((DropShadow) letter.getEffect()).setRadius(10.0);
                });
            }
        }
    }

    
    private void setupYearText(@SuppressWarnings("unused") Font unused) {
        Font yearFont = loadYearFont();
        yearText.setFont(yearFont);
        yearText.setFill(Color.WHITE);

        try {
            yearText.setStyle("-fx-font-size: 20px; -fx-font-family: '" + yearFont.getName() + "';");
        } catch (Exception ignored1) {
            yearText.setStyle("-fx-font-size: 20px;");
        }

        
        Runnable positionYear = () -> {
            Platform.runLater(() -> {
                try {
                    double titleX = titleContainer.getLayoutX() + titleContainer.getTranslateX();
                    double titleY = titleContainer.getLayoutY() + titleContainer.getTranslateY();
                    double titleW = titleContainer.getBoundsInParent().getWidth();
                    double yearW = yearText.getBoundsInParent().getWidth();

                    yearText.setLayoutX(titleX + Math.max(0, (titleW - yearW) / 2.0));
                    yearText.setLayoutY(titleY + titleContainer.getBoundsInParent().getHeight() + 6);
                } catch (Exception ignored2) {
                    
                }
            });
        };

        positionYear.run();

        
        titleContainer.boundsInParentProperty().addListener((obs, oldVal, newVal) -> positionYear.run());
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> positionYear.run());
    }

    
    private Font loadYearFont() {
        String[] fontPaths = {
                "/fonts/PressStart2P-vaV7.ttf",
                "/fonts/AXR ArcadeMachine.ttf",
                "/fonts/Arcadia-SVG.ttf",
                "/fonts/NineByFiveNbp-MypB.ttf",
                "/fonts/digital.ttf"
        };

        for (String fontPath : fontPaths) {
            try {
                Font font = Font.loadFont(getClass().getResourceAsStream(fontPath), 40);
                if (font != null) {
                    return font;
                }
            } catch (Exception ignored) {
                
            }
        }

        return Font.font("Arial", 40);
    }

    
    public void stopAnimations() {
        for (Timeline flicker : titleFlickers) {
            flicker.stop();
        }
    }
}



