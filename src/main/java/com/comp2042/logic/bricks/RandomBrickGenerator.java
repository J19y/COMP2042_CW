package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    private static final int QUEUE_SIZE = 2;

    
     // Creates a new RandomBrickGenerator and initializes the brick queue
     
    public RandomBrickGenerator() {
        brickList = createBrickList();
        initializeNextBricks();
    }

    
     // Creates the list of available brick types
     
    private List<Brick> createBrickList() {
        List<Brick> bricks = new ArrayList<>();
        bricks.add(new IBrick());
        bricks.add(new JBrick());
        bricks.add(new LBrick());
        bricks.add(new OBrick());
        bricks.add(new SBrick());
        bricks.add(new TBrick());
        bricks.add(new ZBrick());
        return bricks;
    }

    
      // Initializes the queue of next bricks
     
    private void initializeNextBricks() {
        for (int i = 0; i < QUEUE_SIZE; i++) {
            addNextRandomBrick();
        }
    }

    
     // Adds a new random brick to the queue
     
    private void addNextRandomBrick() {
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
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
