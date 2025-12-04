package com.comp2042.tetris.engine.bricks;


public interface BrickGenerator {

    Brick getBrick();
    
    Brick peekNextBrick();

    
    
    java.util.List<Brick> peekNextBricks(int count);
}

