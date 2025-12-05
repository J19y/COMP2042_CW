package com.comp2042.tetris.engine.bricks;

import java.util.List;

/**
 * Interface representing a Tetris brick (tetromino).
 * <p>
 * Each brick implementation provides its shape as a list of rotation matrices.
 * Standard Tetris has 7 brick types: I, J, L, O, S, T, and Z.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public interface Brick {
    
    /**
     * Returns the list of rotation matrices for this brick.
     * <p>
     * Each matrix represents one rotation state. Most bricks have 4 rotations,
     * while the O brick (square) has only 1.
     * </p>
     *
     * @return a list of 2D arrays representing each rotation state
     */
    List<int[][]> getRotationMatrix();
}

