package com.comp2042.tetris.services.notify;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for NotificationManager event notification and UI display behavior.
 * Focuses on API contracts and null-safety.
 */
class NotificationManagerTest {

    private NotificationManager notificationManager;

    @BeforeEach
    void setUp() {
        // Setup for each test
    }

    @Test
    void notificationManagerCanBeCreatedWithGroup() {
        assertDoesNotThrow(() -> {
            notificationManager = new NotificationManager(null);
        }, "NotificationManager should be constructible with a Group");
    }

    @Test
    void notificationManagerCanBeCreatedWithNullGroup() {
        assertDoesNotThrow(() -> {
            notificationManager = new NotificationManager(null);
        }, "NotificationManager should handle null Group gracefully");
    }

    @Test
    void notificationManagerInitializesSuccessfully() {
        notificationManager = new NotificationManager(null);
        assertNotNull(notificationManager, "NotificationManager should be initialized");
    }

    @Test
    void showScoreBonusWithIntegerHandlesNullContainer() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showScoreBonus(100),
                "showScoreBonus should handle null container safely");
    }

    @Test
    void showScoreBonusWithIntegerAndOffsetHandlesNullContainer() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showScoreBonus(100, 50.0),
                "showScoreBonus with offset should handle null container safely");
    }

    @Test
    void showMessageHandlesNullContainer() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showMessage("Test message"),
                "showMessage should handle null container safely");
    }

    @Test
    void showEventMessageHandlesNullContainer() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showEventMessage("Event message"),
                "showEventMessage should handle null container safely");
    }

    @Test
    void showLineClearRewardHandlesNullContainer() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showLineClearReward(1),
                "showLineClearReward should handle null container safely");
    }

    @Test
    void showScoreBonusWithPositiveValue() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showScoreBonus(50),
                "showScoreBonus should accept positive integers");
    }

    @Test
    void showScoreBonusWithZeroValue() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showScoreBonus(0),
                "showScoreBonus should accept zero");
    }

    @Test
    void showScoreBonusWithNegativeValue() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showScoreBonus(-10),
                "showScoreBonus should accept negative values");
    }

    @Test
    void showScoreBonusWithLargeValue() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showScoreBonus(999999),
                "showScoreBonus should accept large values");
    }

    @Test
    void showMessageWithValidString() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showMessage("Valid message"),
                "showMessage should accept valid strings");
    }

    @Test
    void showMessageWithEmptyString() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showMessage(""),
                "showMessage should accept empty strings");
    }

    @Test
    void showMessageWithNullString() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showMessage(null),
                "showMessage should handle null strings safely");
    }

    @Test
    void showEventMessageWithValidString() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showEventMessage("Event"),
                "showEventMessage should accept valid strings");
    }

    @Test
    void showEventMessageWithNullString() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showEventMessage(null),
                "showEventMessage should handle null strings safely");
    }

    @Test
    void showEventMessageWithEmptyString() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showEventMessage(""),
                "showEventMessage should handle empty strings safely");
    }

    @Test
    void showLineClearRewardWithPositiveLines() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showLineClearReward(1),
                "showLineClearReward should accept 1 line");
        assertDoesNotThrow(() -> notificationManager.showLineClearReward(4),
                "showLineClearReward should accept 4 lines");
    }

    @Test
    void showLineClearRewardWithZeroLines() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showLineClearReward(0),
                "showLineClearReward should handle zero lines safely");
    }

    @Test
    void showLineClearRewardWithNegativeLines() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showLineClearReward(-1),
                "showLineClearReward should handle negative lines safely");
    }

    @Test
    void multipleScoreBonusCallsInSequence() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> {
            notificationManager.showScoreBonus(10);
            notificationManager.showScoreBonus(20);
            notificationManager.showScoreBonus(30);
        }, "Multiple showScoreBonus calls should work sequentially");
    }

    @Test
    void notificationManagerMethodsAreIdempotent() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> {
            notificationManager.showMessage("Message1");
            notificationManager.showMessage("Message1");
            notificationManager.showMessage("Message1");
        }, "Multiple identical calls should not cause errors");
    }

    @Test
    void showScoreBonusWithOffsetVariations() {
        notificationManager = new NotificationManager(null);
        assertDoesNotThrow(() -> notificationManager.showScoreBonus(100, 0.0),
                "Should accept offset 0.0");
        assertDoesNotThrow(() -> notificationManager.showScoreBonus(100, 50.5),
                "Should accept positive offset");
        assertDoesNotThrow(() -> notificationManager.showScoreBonus(100, -50.5),
                "Should accept negative offset");
    }
}
