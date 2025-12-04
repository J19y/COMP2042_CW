package com.comp2042.tetris.ui.view;

import com.comp2042.tetris.services.audio.MusicManager;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AudioSettingsController {

    private static final Logger LOGGER = Logger.getLogger(AudioSettingsController.class.getName());

    private final Slider volumeSlider;
    private final Text volumeText;
    private final Button musicToggleButton;

    private int volume = 100;
    private boolean musicOn = true;

    public AudioSettingsController(Slider volumeSlider, Text volumeText, Button musicToggleButton) {
        this.volumeSlider = volumeSlider;
        this.volumeText = volumeText;
        this.musicToggleButton = musicToggleButton;
    }

    public void initialize() {
        if (volumeSlider != null) {
            volumeSlider.setMin(0);
            volumeSlider.setMax(100);
            volumeSlider.setValue(volume);
            volumeSlider.setBlockIncrement(1);
            volumeSlider.valueProperty().addListener((obs, oldV, newV) -> {
                if (newV == null) {
                    return;
                }
                volume = newV.intValue();
                updateVolumeText();
            });
        }
        if (musicToggleButton != null) {
            musicToggleButton.setText(musicOn ? "ON" : "OFF");
            if (volumeSlider != null) {
                volumeSlider.setDisable(!musicOn);
            }
        }
        updateVolumeText();
        try {
            MusicManager.getInstance().setMusicEnabled(musicOn);
        } catch (Exception ignored) {
            LOGGER.log(Level.FINE, "Unable to initialize music state", ignored);
        }
    }

    public void increaseVolume() {
        if (volume >= 100) {
            return;
        }
        volume = Math.min(100, volume + 10);
        updateVolumeText();
        playButtonClick();
    }

    public void decreaseVolume() {
        if (volume <= 0) {
            return;
        }
        volume = Math.max(0, volume - 10);
        updateVolumeText();
        playButtonClick();
    }

    public void toggleMusic() {
        musicOn = !musicOn;
        if (musicToggleButton != null) {
            musicToggleButton.setText(musicOn ? "ON" : "OFF");
        }
        if (volumeSlider != null) {
            volumeSlider.setDisable(!musicOn);
        }
        if (!musicOn && volumeText != null) {
            volumeText.setText("OFF");
        } else {
            updateVolumeText();
        }
        try {
            MusicManager.getInstance().setMusicEnabled(musicOn);
        } catch (Exception ignored) {
            LOGGER.log(Level.FINE, "Unable to toggle music", ignored);
        }
    }

    private void updateVolumeText() {
        if (volumeText != null && musicOn) {
            volumeText.setText(volume + "%");
        }
        try {
            MusicManager.getInstance().setMusicVolume(volume / 100.0);
        } catch (Exception ignored) {
            LOGGER.log(Level.FINE, "Unable to update music volume", ignored);
        }
    }

    private void playButtonClick() {
        try {
            MusicManager.getInstance().playSfx("/audio/ButtonClickingEffect.mp3");
        } catch (Exception ignored) {
            LOGGER.log(Level.FINE, "Unable to play button clicking sfx", ignored);
        }
    }
}
