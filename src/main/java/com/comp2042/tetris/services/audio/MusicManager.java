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

public class MusicManager {

    public enum Track { MAIN_MENU, CLASSIC, RUSH, MYSTERY, GAME_OVER }

    private static MusicManager instance;

    public static synchronized MusicManager getInstance() {
        if (instance == null) instance = new MusicManager();
        return instance;
    }

    private final Map<Track, String> trackMap = new HashMap<>();
    private MediaPlayer currentPlayer;
    private Track currentTrack;
    // Remember the last track when music is muted so unmuting can resume it
    private Track lastTrack = null;
    // If we pause the media player because of a mute action we track that so we can resume instead of restarting
    private boolean pausedByMute = false;
    private double musicVolume = 1.0; // 0..1
    private double sfxVolume = 1.0; // 0..1
    private boolean musicEnabled = true;
    // Cache AudioClip instances to avoid GC dropping playback and reduce load latency
    private final Map<String, AudioClip> sfxCache = new HashMap<>();

    private MusicManager() {
        // map tracks to resource paths
        trackMap.put(Track.MAIN_MENU, "/audio/MainMenuSoundTrack.mp3");
        trackMap.put(Track.CLASSIC, "/audio/ClassicModeSoundTrack.mp3");
        trackMap.put(Track.RUSH, "/audio/RushModeSoundTrack.mp3");
        trackMap.put(Track.MYSTERY, "/audio/MysteryModeSoundTrack.mp3");
        trackMap.put(Track.GAME_OVER, "/audio/GameOverEffect.mp3");
    }

    public void playTrack(Track track, long fadeInMillis) {
        playTrack(track, fadeInMillis, -1);
    }

    /**
     * Play a track with optional repeat count. If repeatCount &lt;= 0 the track will loop indefinitely.
     */
    public void playTrack(Track track, long fadeInMillis, int repeatCount) {
        if (!musicEnabled) return;
        if (track == currentTrack) return;

        String path = trackMap.get(track);
        if (path == null) return;

        Platform.runLater(() -> {
            // fade out current
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
        // set start value immediately
        player.setVolume(from);
        t.play();
        return t;
    }

    public void setMusicVolume(double vol) {
        musicVolume = clamp01(vol);
        Platform.runLater(() -> {
            if (currentPlayer != null) currentPlayer.setVolume(musicVolume);
        });
    }

    public double getMusicVolume() { return musicVolume; }

    public void setSfxVolume(double vol) { sfxVolume = clamp01(vol); }

    public double getSfxVolume() { return sfxVolume; }

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
                // Stop any previous instance of this clip to avoid overlap (useful for repeating ticks)
                try { clip.stop(); } catch (Exception ignored) {}
                clip.play();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Play an SFX with a per-call volume multiplier (relative to the configured sfxVolume).
     * multiplier > 1.0 will increase loudness (clamped to 1.0).
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
                // Stop previous instance to prevent overlapping sounds when playing the same SFX repeatedly
                try { clip.stop(); } catch (Exception ignored) {}
                clip.play();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Play an SFX immediately at the configured SFX volume (no stop-delay or fade).
     * Useful for short UI/feedback sounds that must be heard instantly (e.g. GO countdown).
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
                // Use the overload that takes a volume to ensure immediate playback at the desired level
                clip.play(vol);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Play an SFX immediately with a per-call multiplier, useful to make GO louder.
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
     * Play an SFX immediately at an absolute volume (0..1), ignoring the sfxVolume multiplier.
     * Useful when a particular clip must be louder regardless of master SFX level.
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
     * Stop playback of a cached SFX clip if playing.
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
     * Fade current music player volume to `toVol` over `millis` milliseconds.
     */
    public void fadeMusicTo(double toVol, long millis) {
        final double to = clamp01(toVol);
        Platform.runLater(() -> {
            if (currentPlayer == null) return;
            fadeVolume(currentPlayer, currentPlayer.getVolume(), to, Math.max(0, millis));
            // Update stored musicVolume so subsequent playTrack/sets use the updated value
            musicVolume = to;
        });
    }

    public void stop(long fadeOutMillis) {
        Platform.runLater(() -> {
            if (currentPlayer != null) {
                fadeOutAndStop(currentPlayer, fadeOutMillis);
                currentPlayer = null;
                currentTrack = null;
            }
        });
    }

    public void setMusicEnabled(boolean enabled) {
        // If disabling: pause the current player (preserve position) and mark it pausedByMute.
        if (!enabled) {
            // remember which track was current so we can restore if the player is disposed
            lastTrack = currentTrack;
            musicEnabled = false;
            pausedByMute = false;
            if (currentPlayer != null) {
                try {
                    // Only pause if it's currently playing; if already stopped, leave state alone
                    if (currentPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        currentPlayer.pause();
                        pausedByMute = true;
                    }
                } catch (Exception ignored) {}
            }
            return;
        }

        // Enabling: if we paused previously because of mute and the player still exists, resume it.
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

        // If there is no in-memory player but we have a remembered track, fall back to starting it again.
        if (currentPlayer == null && lastTrack != null) {
            try {
                playTrack(lastTrack, 600);
            } catch (Exception ignored) {}
            lastTrack = null;
        }
    }

    public boolean isMusicEnabled() { return musicEnabled; }

    private double clamp01(double v) { return Math.max(0.0, Math.min(1.0, v)); }
}
