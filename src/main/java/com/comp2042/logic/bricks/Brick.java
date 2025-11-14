package com.comp2042.logic.bricks;

import java.util.List;

public interface Brick {
    // Returns the list of rotation matrices for this brick.
    List<int[][]> getRotationMatrix();
}
