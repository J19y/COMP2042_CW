package com.comp2042.tetris.services.audio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests for MusicManager singleton behavior and API contract.
 * Disabled: All methods wrap calls with Platform.runLater() which requires JavaFX Toolkit.
 */
@Disabled("MusicManager requires JavaFX Toolkit - all volume/track operations use Platform.runLater()")
class MusicManagerAPITest {

    @Test
    void singletonInstanceConsistent() {
        MusicManager manager1 = MusicManager.getInstance();
        MusicManager manager2 = MusicManager.getInstance();

        assertTrue(manager1 == manager2, "getInstance should return same instance");
    }

    @Test
    void musicEnabledByDefault() {
        MusicManager manager = MusicManager.getInstance();
        assertTrue(manager.isMusicEnabled(), "Music should be enabled by default");
    }

    @Test
    void setMusicVolumeClampsToZeroOne() {
        MusicManager manager = MusicManager.getInstance();

        manager.setMusicVolume(-0.5);
        assertEquals(0.0, manager.getMusicVolume(), 0.01, "Negative volume should clamp to 0");

        manager.setMusicVolume(1.5);
        assertEquals(1.0, manager.getMusicVolume(), 0.01, "Volume > 1 should clamp to 1");
    }

    @Test
    void setSfxVolumeClampsToZeroOne() {
        MusicManager manager = MusicManager.getInstance();

        manager.setSfxVolume(-0.1);
        assertEquals(0.0, manager.getSfxVolume(), 0.01, "Negative SFX volume should clamp to 0");

        manager.setSfxVolume(2.0);
        assertEquals(1.0, manager.getSfxVolume(), 0.01, "SFX volume > 1 should clamp to 1");
    }

    @Test
    void setMusicVolumeAcceptsValidValues() {
        MusicManager manager = MusicManager.getInstance();

        manager.setMusicVolume(0.5);
        assertEquals(0.5, manager.getMusicVolume(), 0.01, "Should accept valid volumes");

        manager.setMusicVolume(0.0);
        assertEquals(0.0, manager.getMusicVolume(), 0.01, "Should accept zero volume");

        manager.setMusicVolume(1.0);
        assertEquals(1.0, manager.getMusicVolume(), 0.01, "Should accept full volume");
    }

    @Test
    void setSfxVolumeAcceptsValidValues() {
        MusicManager manager = MusicManager.getInstance();

        manager.setSfxVolume(0.75);
        assertEquals(0.75, manager.getSfxVolume(), 0.01, "Should accept valid SFX volumes");
    }

    @Test
    void musicEnableDisable() {
        MusicManager manager = MusicManager.getInstance();

        manager.setMusicEnabled(false);
        assertTrue(!manager.isMusicEnabled(), "Music should be disabled");

        manager.setMusicEnabled(true);
        assertTrue(manager.isMusicEnabled(), "Music should be re-enabled");
    }

    @Test
    void trackEnumHasRequiredTracks() {
        assertNotNull(MusicManager.Track.MAIN_MENU);
        assertNotNull(MusicManager.Track.CLASSIC);
        assertNotNull(MusicManager.Track.RUSH);
        assertNotNull(MusicManager.Track.MYSTERY);
        assertNotNull(MusicManager.Track.GAME_OVER);
    }

    @Test
    void multipleVolumeChanges() {
        MusicManager manager = MusicManager.getInstance();

        manager.setMusicVolume(0.2);
        manager.setMusicVolume(0.4);
        manager.setMusicVolume(0.6);

        assertEquals(0.6, manager.getMusicVolume(), 0.01, "Last volume setting should win");
    }

    @Test
    void sfxAndMusicVolumesIndependent() {
        MusicManager manager = MusicManager.getInstance();

        manager.setMusicVolume(0.3);
        manager.setSfxVolume(0.7);

        assertEquals(0.3, manager.getMusicVolume(), 0.01, "Music volume should be 0.3");
        assertEquals(0.7, manager.getSfxVolume(), 0.01, "SFX volume should be 0.7");
    }
}
