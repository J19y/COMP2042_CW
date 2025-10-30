package com.comp2042.logic.bricks;

import java.util.List;

public interface Brick {
    /**
     * Previously named getShapeMatrix(). Renamed to getRotationMatrix() to make
     * it explicit that the method returns the set of rotation matrices for the brick.
     */
    List<int[][]> getRotationMatrix();
}
