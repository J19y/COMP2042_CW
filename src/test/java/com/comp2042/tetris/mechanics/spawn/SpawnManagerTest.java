package com.comp2042.tetris.mechanics.spawn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.comp2042.tetris.domain.model.SpawnResult;

class SpawnManagerTest {

    @Test
    void spawnReturnsFalseWhenGameContinues() {
        StubBrickSpawn spawner = new StubBrickSpawn(false);
        SpawnManager manager = new SpawnManager(spawner);
        AtomicInteger callbackHits = new AtomicInteger();

        boolean gameOver = manager.spawn(callbackHits::incrementAndGet);

        // Regular spawns should not trigger callbacks or observers
        assertFalse(gameOver);
        assertEquals(0, callbackHits.get());
    }

    @Test
    void spawnNotifiesCallbackAndObserversOnGameOver() {
        StubBrickSpawn spawner = new StubBrickSpawn(true);
        SpawnManager manager = new SpawnManager(spawner);
        AtomicInteger callbackHits = new AtomicInteger();
        AtomicInteger observerHits = new AtomicInteger();

        manager.addGameOverObserver(observerHits::incrementAndGet);
        manager.addGameOverObserver(observerHits::incrementAndGet);

        boolean gameOver = manager.spawn(callbackHits::incrementAndGet);

        // A game over should alert both the direct callback and all observers
        assertTrue(gameOver);
        assertEquals(1, callbackHits.get());
        assertEquals(2, observerHits.get());
    }

    @Test
    void removingObserverStopsNotifications() {
        StubBrickSpawn spawner = new StubBrickSpawn(true);
        SpawnManager manager = new SpawnManager(spawner);
        AtomicInteger observerHits = new AtomicInteger();
        SpawnManager.GameOverCallback observer = observerHits::incrementAndGet;

        manager.addGameOverObserver(observer);
        manager.removeGameOverObserver(observer);

        manager.spawn();

        // Once detached, the listener should stay silent
        assertEquals(0, observerHits.get());
    }

    @Test
    void observerExceptionsDoNotBlockOthers() {
        StubBrickSpawn spawner = new StubBrickSpawn(true);
        SpawnManager manager = new SpawnManager(spawner);
        List<String> notifications = new ArrayList<>();

        manager.addGameOverObserver(() -> {
            notifications.add("flaky");
            throw new RuntimeException("boom");
        });
        manager.addGameOverObserver(() -> notifications.add("healthy"));

        manager.spawn();

        // Even if one observer blows up, the rest should still hear about it
        assertEquals(List.of("flaky", "healthy"), notifications);
    }

    private static final class StubBrickSpawn implements BrickSpawn {
        private boolean nextGameOver;

        private StubBrickSpawn(boolean nextGameOver) {
            this.nextGameOver = nextGameOver;
        }

        @Override
        public SpawnResult spawnBrick() {
            return new SpawnResult(nextGameOver);
        }
    }
}
