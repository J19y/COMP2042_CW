package com.comp2042.tetris.mechanics.bricks;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Supplier<Brick>> brickSuppliers;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    private static final int QUEUE_SIZE = 5;

    
    public RandomBrickGenerator() {
        this.brickSuppliers = BrickRegistry.getInstance().suppliers();
        if (brickSuppliers.isEmpty()) {
            throw new IllegalStateException("No bricks registered in BrickRegistry");
        }
        initializeNextBricks();
    }

    
    private void initializeNextBricks() {
        for (int i = 0; i < QUEUE_SIZE; i++) {
            addNextRandomBrick();
        }
    }

    
    private void addNextRandomBrick() {
        Supplier<Brick> supplier = brickSuppliers.get(
            ThreadLocalRandom.current().nextInt(brickSuppliers.size()));
        nextBricks.add(supplier.get());
    }

    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= QUEUE_SIZE) {
            addNextRandomBrick();
        }
        return nextBricks.poll();
    }

    @Override
    public Brick peekNextBrick() {
        return nextBricks.peek();
    }

    @Override
    public List<Brick> peekNextBricks(int count) {
        List<Brick> result = new java.util.ArrayList<>();
        
        while (nextBricks.size() < count) {
            addNextRandomBrick();
        }
        
        int i = 0;
        for (Brick b : nextBricks) {
            if (i >= count) break;
            result.add(b);
            i++;
        }
        return result;
    }
}

