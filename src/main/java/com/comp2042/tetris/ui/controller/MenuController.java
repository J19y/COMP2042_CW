package com.comp2042.tetris.ui.controller;

import com.comp2042.tetris.services.audio.MusicManager;
import com.comp2042.tetris.ui.animation.BackgroundEffectsManager;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * FXML controller for the main menu screen.
 * Manages menu UI including title animations, button interactions,
 * settings panel, level selection, and navigation to game modes.
 * Delegates to specialized managers for different UI sections.
 *
 */
public class MenuController {

    
    @FXML
    private Pane backgroundPane;
    @FXML
    private StackPane rootPane;
    @FXML
    private BorderPane mainMenuUI;

    @FXML
    private HBox titleContainer;
    @FXML
    private Text yearText;

    @FXML
    private Button playButton;
    @FXML
    private Button quitButton;
    @FXML
    private StackPane exitConfirmationOverlay;
    @FXML
    private StackPane controlLight;
    @FXML
    private javafx.scene.image.ImageView controlLightImage;
    @FXML
    private javafx.scene.Group controlLightVector;
    @FXML
    private Button controlsButton;
    @FXML
    private StackPane settingsOverlay;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Button musicToggleButton;
    @FXML
    private Text volumeText;
    @FXML
    private Button settingsButton;

    
    private BackgroundEffectsManager backgroundEffectsManager;
    private TitleSetupManager titleSetupManager;
    private ButtonSetupManager buttonSetupManager;
    private ControlPanelManager controlPanelManager;
    private SettingsPanelManager settingsPanelManager;
    private LevelSelectionManager levelSelectionManager;
    private MenuAnimationController menuAnimationController;
    private GameSceneLoader gameSceneLoader;

    
    @FXML
    public void initialize() {
        
        initializeManagers();

        
        titleSetupManager.setupTitle();
        buttonSetupManager.setupButtons();
        buttonSetupManager.setupControlLight();
        buttonSetupManager.setupControlsButton();
        buttonSetupManager.setupSettingsButton();
        settingsPanelManager.setupVolumeControl();

        
        backgroundEffectsManager.startAnimation();

        
        menuAnimationController.prepareLaunchForAnimation();
        if (menuAnimationController.shouldPlayLaunchAnimation()) {
            menuAnimationController.playLaunchAnimation();
        } else {
            menuAnimationController.finalizeLaunchState();
        }

        
        Rectangle fadeOverlay = new Rectangle(700, 600, javafx.scene.paint.Color.BLACK);
        fadeOverlay.setMouseTransparent(true);
        rootPane.getChildren().add(fadeOverlay);
       
        if (mainMenuUI != null) {
            mainMenuUI.setOpacity(0.0);
            FadeTransition menuFade = new FadeTransition(Duration.seconds(1.8), mainMenuUI);
            menuFade.setFromValue(0.0);
            menuFade.setToValue(1.0);
            menuFade.setDelay(Duration.millis(220));
            menuFade.play();
        }

        FadeTransition fade = new FadeTransition(Duration.seconds(3), fadeOverlay);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> rootPane.getChildren().remove(fadeOverlay));
        fade.play();

        
        try {
            double initialVol = (volumeSlider != null) ? volumeSlider.getValue() / 100.0 : 1.0;
            MusicManager.getInstance().setMusicVolume(initialVol);
            MusicManager.getInstance().setMusicEnabled(true);
            MusicManager.getInstance().playTrack(MusicManager.Track.MAIN_MENU, 3000);
        } catch (Exception ignored) {
        }
    }

    
    private void initializeManagers() {
        backgroundEffectsManager = new BackgroundEffectsManager(backgroundPane);
        titleSetupManager = new TitleSetupManager(titleContainer, yearText, rootPane);
        buttonSetupManager = new ButtonSetupManager(
                playButton, quitButton, controlsButton, settingsButton,
                controlLight, controlLightImage, controlLightVector
        );
        
        VBox levelSelectionContainer = new VBox();
        controlPanelManager = new ControlPanelManager(rootPane, backgroundPane, levelSelectionContainer);
        
        controlPanelManager.setNodesToBlur(titleContainer, playButton, quitButton, controlsButton, settingsButton, yearText);
        settingsPanelManager = new SettingsPanelManager(
                rootPane, settingsOverlay, volumeSlider, musicToggleButton, volumeText, levelSelectionContainer
        );
        levelSelectionManager = new LevelSelectionManager(rootPane, titleContainer, yearText, mainMenuUI, playButton, quitButton, controlsButton, settingsButton);
        menuAnimationController = new MenuAnimationController(
                rootPane, titleContainer, yearText, playButton, quitButton, controlsButton, settingsButton, mainMenuUI
        );
        gameSceneLoader = new GameSceneLoader(playButton, menuAnimationController);

        
        levelSelectionManager.setOnModeSelected(() -> {
            String selectedMode = levelSelectionManager.getSelectedGameMode();
            if (selectedMode != null) {
                gameSceneLoader.loadGameSceneWithMode(selectedMode);
            }
        });
    }

    
    @FXML
    private void onPlay() {
        levelSelectionManager.showLevelSelection();
    }

    
    @FXML
    private void onQuit() {
        menuAnimationController.blurMenuUI();
        exitConfirmationOverlay.setVisible(true);
    }

    
    @FXML
    private void onConfirmQuit() {
        javafx.stage.Stage stage = (javafx.stage.Stage) quitButton.getScene().getWindow();
        stage.close();
    }

    
    @FXML
    private void onCancelQuit() {
        menuAnimationController.clearMenuBlur();
        exitConfirmationOverlay.setVisible(false);
    }

    
    @FXML
    public void toggleControlsPanel() {
        controlPanelManager.toggleControllerPanel();
    }

    
    @FXML
    public void openControllerPanel() {
        controlPanelManager.openControllerPanel();
    }

    
    @FXML
    private void toggleSettings() {
        settingsPanelManager.toggleSettings();
    }

    
    @FXML
    private void toggleMusic() {
        settingsPanelManager.toggleMusic();
    }

    
    @FXML
    private void closeSettings() {
        settingsPanelManager.closeSettings();
    }
}




