package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Supplier<Brick>> brickSuppliers;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    private static final int QUEUE_SIZE = 2;

    // Creates a new RandomBrickGenerator and initializes the brick queue
    public RandomBrickGenerator() {
        this.brickSuppliers = BrickRegistry.getSuppliers();
        if (brickSuppliers.isEmpty()) {
            throw new IllegalStateException("No bricks registered in BrickRegistry");
        }
        initializeNextBricks();
    }

    // Initializes the queue of next bricks
    private void initializeNextBricks() {
        for (int i = 0; i < QUEUE_SIZE; i++) {
            addNextRandomBrick();
        }
    }

    // Adds a new random brick instance to the queue
    private void addNextRandomBrick() {
        Supplier<Brick> supplier = brickSuppliers.get(
            ThreadLocalRandom.current().nextInt(brickSuppliers.size()));
        nextBricks.add(supplier.get());
    }

    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 1) {
            addNextRandomBrick();
        }
        return nextBricks.poll();
    }

    @Override
    public Brick peekNextBrick() {
        return nextBricks.peek();
    }
}
