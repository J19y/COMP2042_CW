package com.comp2042.tetris.services.audio;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Singleton manager for background music playback, volume control, fade transitions, and sound effects.
 * <p>
 * Provides methods to play music tracks with fading, control volume, and play
 * sound effects. Supports different tracks for each game mode.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public class MusicManager {

    /**
     * Enum representing available music tracks.
     */
    public enum Track { MAIN_MENU, CLASSIC, RUSH, MYSTERY, GAME_OVER }

    private static MusicManager instance;

    /**
     * Returns the singleton instance of MusicManager.
     *
     * @return the MusicManager instance
     */
    public static synchronized MusicManager getInstance() {
        if (instance == null) instance = new MusicManager();
        return instance;
    }

    private final Map<Track, String> trackMap = new HashMap<>();
    private MediaPlayer currentPlayer;
    private Track currentTrack;
    
    private Track lastTrack = null;
    
    private boolean pausedByMute = false;
    private double musicVolume = 1.0; 
    private double sfxVolume = 1.0; 
    private boolean musicEnabled = true;
    
    private final Map<String, AudioClip> sfxCache = new HashMap<>();

    private MusicManager() {
        
        trackMap.put(Track.MAIN_MENU, "/audio/MainMenuSoundTrack.mp3");
        trackMap.put(Track.CLASSIC, "/audio/ClassicModeSoundTrack.mp3");
        trackMap.put(Track.RUSH, "/audio/RushModeSoundTrack.mp3");
        trackMap.put(Track.MYSTERY, "/audio/MysteryModeSoundTrack.mp3");
        trackMap.put(Track.GAME_OVER, "/audio/GameOverEffect.mp3");
    }

    /**
     * Plays a music track with fade-in effect, looping indefinitely.
     *
     * @param track the track to play
     * @param fadeInMillis duration of fade-in in milliseconds
     */
    public void playTrack(Track track, long fadeInMillis) {
        playTrack(track, fadeInMillis, -1);
    }

    /**
     * Plays a music track with fade-in effect.
     *
     * @param track the track to play
     * @param fadeInMillis duration of fade-in in milliseconds
     * @param repeatCount number of times to repeat (-1 for infinite)
     */
    public void playTrack(Track track, long fadeInMillis, int repeatCount) {
        if (!musicEnabled) return;
        if (track == currentTrack) return;

        String path = trackMap.get(track);
        if (path == null) return;

        Platform.runLater(() -> {
            
            if (currentPlayer != null) {
                fadeOutAndStop(currentPlayer, 400);
            }

            try {
                URL res = getClass().getResource(path);
                if (res == null) return;
                Media media = new Media(res.toExternalForm());
                MediaPlayer player = new MediaPlayer(media);
                if (repeatCount <= 0) {
                    player.setCycleCount(MediaPlayer.INDEFINITE);
                } else {
                    player.setCycleCount(repeatCount);
                }
                player.setVolume(0);
                currentPlayer = player;
                currentTrack = track;
                player.setOnReady(() -> {
                    player.play();
                    fadeVolume(player, 0.0, musicVolume, Math.max(0, fadeInMillis));
                });
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void fadeOutAndStop(MediaPlayer player, long fadeMillis) {
        if (player == null) return;
        fadeVolume(player, player.getVolume(), 0.0, fadeMillis).setOnFinished(e -> {
            try {
                player.stop();
                player.dispose();
            } catch (Exception ignored) {}
        });
    }

    private Timeline fadeVolume(MediaPlayer player, double from, double to, long millis) {
        if (player == null) return new Timeline();
        Timeline t = new Timeline();
        t.getKeyFrames().add(new KeyFrame(Duration.millis(millis), new KeyValue(player.volumeProperty(), to)));
        
        player.setVolume(from);
        t.play();
        return t;
    }

    /**
     * Sets the music volume.
     *
     * @param vol volume level (0.0 to 1.0)
     */
    public void setMusicVolume(double vol) {
        musicVolume = clamp01(vol);
        Platform.runLater(() -> {
            if (currentPlayer != null) currentPlayer.setVolume(musicVolume);
        });
    }

    /**
     * Gets the current music volume.
     *
     * @return the music volume (0.0 to 1.0)
     */
    public double getMusicVolume() { return musicVolume; }

    /**
     * Sets the sound effects volume.
     *
     * @param vol volume level (0.0 to 1.0)
     */
    public void setSfxVolume(double vol) { sfxVolume = clamp01(vol); }

    /**
     * Gets the current sound effects volume.
     *
     * @return the SFX volume (0.0 to 1.0)
     */
    public double getSfxVolume() { return sfxVolume; }

    /**
     * Plays a sound effect at the current SFX volume.
     *
     * @param resourcePath the resource path to the audio file
     */
    public void playSfx(String resourcePath) {
        String path = resourcePath;
        Platform.runLater(() -> {
            try {
                URL res = getClass().getResource(path);
                if (res == null) {
                    System.out.println("[MusicManager] SFX resource not found: " + path);
                    return;
                }
                String key = res.toExternalForm();
                AudioClip clip = sfxCache.get(key);
                if (clip == null) {
                    clip = new AudioClip(key);
                    sfxCache.put(key, clip);
                }
                clip.setVolume(sfxVolume);
                System.out.println("[MusicManager] playSfx: " + path + " volume=" + sfxVolume);
                
                try { clip.stop(); } catch (Exception ignored) {}
                clip.play();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Plays a sound effect with volume multiplier.
     *
     * @param resourcePath the resource path to the audio file
     * @param multiplier volume multiplier (0.0 to 1.0)
     */
    public void playSfx(String resourcePath, double multiplier) {
        final double mult = clamp01(multiplier);
        String path = resourcePath;
        Platform.runLater(() -> {
            try {
                URL res = getClass().getResource(path);
                if (res == null) {
                    System.out.println("[MusicManager] SFX resource not found: " + path);
                    return;
                }
                String key = res.toExternalForm();
                AudioClip clip = sfxCache.get(key);
                if (clip == null) {
                    clip = new AudioClip(key);
                    sfxCache.put(key, clip);
                }
                double vol = clamp01(sfxVolume * mult);
                clip.setVolume(vol);
                System.out.println("[MusicManager] playSfx: " + path + " multiplier=" + mult + " finalVol=" + vol);
                
                try { clip.stop(); } catch (Exception ignored) {}
                clip.play();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Plays a sound effect immediately without caching delay.
     *
     * @param resourcePath the resource path to the audio file
     */
    public void playSfxImmediate(String resourcePath) {
        String path = resourcePath;
        Platform.runLater(() -> {
            try {
                URL res = getClass().getResource(path);
                if (res == null) {
                    System.out.println("[MusicManager] SFX resource not found: " + path);
                    return;
                }
                String key = res.toExternalForm();
                AudioClip clip = sfxCache.get(key);
                if (clip == null) {
                    clip = new AudioClip(key);
                    sfxCache.put(key, clip);
                }
                double vol = sfxVolume;
                System.out.println("[MusicManager] playSfxImmediate: " + path + " vol=" + vol);
                try { clip.stop(); } catch (Exception ignored) {}
                
                clip.play(vol);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Plays a sound effect immediately with volume multiplier.
     *
     * @param resourcePath the resource path to the audio file
     * @param multiplier volume multiplier (0.0 to 1.0)
     */
    public void playSfxImmediate(String resourcePath, double multiplier) {
        final double mult = clamp01(multiplier);
        String path = resourcePath;
        Platform.runLater(() -> {
            try {
                URL res = getClass().getResource(path);
                if (res == null) {
                    System.out.println("[MusicManager] SFX resource not found: " + path);
                    return;
                }
                String key = res.toExternalForm();
                AudioClip clip = sfxCache.get(key);
                if (clip == null) {
                    clip = new AudioClip(key);
                    sfxCache.put(key, clip);
                }
                double vol = clamp01(sfxVolume * mult);
                System.out.println("[MusicManager] playSfxImmediate: " + path + " mult=" + mult + " vol=" + vol);
                try { clip.stop(); } catch (Exception ignored) {}
                clip.play(vol);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Plays a sound effect at an absolute volume level.
     *
     * @param resourcePath the resource path to the audio file
     * @param absoluteVolume the absolute volume level (0.0 to 1.0)
     */
    public void playSfxAtVolume(String resourcePath, double absoluteVolume) {
        final double vol = clamp01(absoluteVolume);
        String path = resourcePath;
        Platform.runLater(() -> {
            try {
                URL res = getClass().getResource(path);
                if (res == null) {
                    System.out.println("[MusicManager] SFX resource not found: " + path);
                    return;
                }
                String key = res.toExternalForm();
                AudioClip clip = sfxCache.get(key);
                if (clip == null) {
                    clip = new AudioClip(key);
                    sfxCache.put(key, clip);
                }
                System.out.println("[MusicManager] playSfxAtVolume: " + path + " vol=" + vol);
                try { clip.stop(); } catch (Exception ignored) {}
                clip.play(vol);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Stops a currently playing sound effect.
     *
     * @param resourcePath the resource path of the audio to stop
     */
    public void stopSfx(String resourcePath) {
        String path = resourcePath;
        Platform.runLater(() -> {
            try {
                URL res = getClass().getResource(path);
                if (res == null) return;
                String key = res.toExternalForm();
                AudioClip clip = sfxCache.get(key);
                if (clip != null) {
                    clip.stop();
                    System.out.println("[MusicManager] stopSfx: " + path);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Fades the music volume to a target level.
     *
     * @param toVol target volume (0.0 to 1.0)
     * @param millis duration of fade in milliseconds
     */
    public void fadeMusicTo(double toVol, long millis) {
        final double to = clamp01(toVol);
        Platform.runLater(() -> {
            if (currentPlayer == null) return;
            fadeVolume(currentPlayer, currentPlayer.getVolume(), to, Math.max(0, millis));
            
            musicVolume = to;
        });
    }

    /**
     * Stops the current music with a fade-out effect.
     *
     * @param fadeOutMillis duration of fade-out in milliseconds
     */
    public void stop(long fadeOutMillis) {
        Platform.runLater(() -> {
            if (currentPlayer != null) {
                fadeOutAndStop(currentPlayer, fadeOutMillis);
                currentPlayer = null;
                currentTrack = null;
            }
        });
    }

    /**
     * Enables or disables music playback.
     * <p>
     * When disabled, pauses the current track. When re-enabled,
     * resumes from the paused position.
     * </p>
     *
     * @param enabled true to enable music, false to disable
     */
    public void setMusicEnabled(boolean enabled) {
        
        if (!enabled) {
            
            lastTrack = currentTrack;
            musicEnabled = false;
            pausedByMute = false;
            if (currentPlayer != null) {
                try {
                    
                    if (currentPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        currentPlayer.pause();
                        pausedByMute = true;
                    }
                } catch (Exception ignored) {}
            }
            return;
        }

        
        musicEnabled = true;
        if (pausedByMute && currentPlayer != null) {
            try {
                currentPlayer.setVolume(musicVolume);
                currentPlayer.play();
            } catch (Exception ignored) {}
            pausedByMute = false;
            lastTrack = null;
            return;
        }

        
        if (currentPlayer == null && lastTrack != null) {
            try {
                playTrack(lastTrack, 600);
            } catch (Exception ignored) {}
            lastTrack = null;
        }
    }

    /**
     * Checks if music is currently enabled.
     *
     * @return {@code true} if music is enabled, {@code false} otherwise
     */
    public boolean isMusicEnabled() { return musicEnabled; }

    private double clamp01(double v) { return Math.max(0.0, Math.min(1.0, v)); }
}

