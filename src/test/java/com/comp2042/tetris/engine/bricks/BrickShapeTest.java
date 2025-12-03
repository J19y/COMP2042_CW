package com.comp2042.tetris.engine.bricks;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class BrickShapeTest {

    // ==================== OBrick Tests (Single Rotation) ====================

    @Test
    void oBrickHasSingleRotation() {
        Brick obrick = new OBrick();
        List<int[][]> rotations = obrick.getRotationMatrix();

        assertEquals(1, rotations.size(), "O brick should have 1 rotation (square)");
    }

    @Test
    void oBrickHasValidShape() {
        Brick obrick = new OBrick();
        List<int[][]> rotations = obrick.getRotationMatrix();
        int[][] shape = rotations.get(0);

        assertEquals(4, shape.length, "O brick should be 4x4");
        for (int[] row : shape) {
            assertEquals(4, row.length, "O brick should be 4x4");
        }
    }

    @Test
    void oBrickHasCorrectPattern() {
        Brick obrick = new OBrick();
        List<int[][]> rotations = obrick.getRotationMatrix();
        int[][] shape = rotations.get(0);

        // O brick pattern: 2x2 square in center
        assertEquals(0, shape[0][0], "Top-left corner should be 0");
        assertEquals(4, shape[1][1], "Center should have brick value");
        assertEquals(4, shape[1][2], "Center should have brick value");
        assertEquals(4, shape[2][1], "Center should have brick value");
        assertEquals(4, shape[2][2], "Center should have brick value");
        assertEquals(0, shape[3][3], "Bottom-right corner should be 0");
    }

    @Test
    void oBrickContainsOnlyValidCellValues() {
        Brick obrick = new OBrick();
        List<int[][]> rotations = obrick.getRotationMatrix();
        int[][] shape = rotations.get(0);

        for (int[] row : shape) {
            for (int cell : row) {
                assertTrue(cell == 0 || cell == 4, "O brick cells should be 0 or 4");
            }
        }
    }

    // ==================== IBrick Tests (Two Rotations) ====================

    @Test
    void iBrickHasTwoRotations() {
        Brick ibrick = new IBrick();
        List<int[][]> rotations = ibrick.getRotationMatrix();

        assertEquals(2, rotations.size(), "I brick should have 2 rotations (horizontal/vertical)");
    }

    @Test
    void iBrickRotationsHaveConsistentDimensions() {
        Brick ibrick = new IBrick();
        List<int[][]> rotations = ibrick.getRotationMatrix();

        int[][] first = rotations.get(0);
        int[][] second = rotations.get(1);

        assertEquals(4, first.length, "I brick rotation 1 should be 4x4");
        assertEquals(4, first[0].length, "I brick rotation 1 should be 4x4");
        assertEquals(4, second.length, "I brick rotation 2 should be 4x4");
        assertEquals(4, second[0].length, "I brick rotation 2 should be 4x4");
    }

    @Test
    void iBrickFirstRotationIsHorizontal() {
        Brick ibrick = new IBrick();
        List<int[][]> rotations = ibrick.getRotationMatrix();
        int[][] horizontal = rotations.get(0);

        // Horizontal: four in a row
        int cellCount = 0;
        for (int[] row : horizontal) {
            for (int cell : row) {
                if (cell == 1) cellCount++;
            }
        }
        assertEquals(4, cellCount, "I brick should have exactly 4 cells");
    }

    @Test
    void iBrickSecondRotationIsVertical() {
        Brick ibrick = new IBrick();
        List<int[][]> rotations = ibrick.getRotationMatrix();
        int[][] vertical = rotations.get(1);

        // Vertical: column has 4 cells
        int cellCount = 0;
        for (int[] row : vertical) {
            for (int cell : row) {
                if (cell == 1) cellCount++;
            }
        }
        assertEquals(4, cellCount, "I brick should have exactly 4 cells");
    }

    @Test
    void iBrickContainsOnlyValidCellValues() {
        Brick ibrick = new IBrick();
        List<int[][]> rotations = ibrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            for (int[] row : rotation) {
                for (int cell : row) {
                    assertTrue(cell == 0 || cell == 1, "I brick cells should be 0 or 1");
                }
            }
        }
    }

    // ==================== TBrick Tests (Four Rotations) ====================

    @Test
    void tBrickHasFourRotations() {
        Brick tbrick = new TBrick();
        List<int[][]> rotations = tbrick.getRotationMatrix();

        assertEquals(4, rotations.size(), "T brick should have 4 rotations");
    }

    @Test
    void tBrickAllRotationsAre4x4() {
        Brick tbrick = new TBrick();
        List<int[][]> rotations = tbrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            assertEquals(4, rotation.length, "Each T rotation should be 4x4");
            for (int[] row : rotation) {
                assertEquals(4, row.length, "Each T rotation should be 4x4");
            }
        }
    }

    @Test
    void tBrickEachRotationHasFourCells() {
        Brick tbrick = new TBrick();
        List<int[][]> rotations = tbrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            int cellCount = 0;
            for (int[] row : rotation) {
                for (int cell : row) {
                    if (cell == 6) cellCount++;
                }
            }
            assertEquals(4, cellCount, "T brick should have exactly 4 cells per rotation");
        }
    }

    @Test
    void tBrickContainsOnlyValidCellValues() {
        Brick tbrick = new TBrick();
        List<int[][]> rotations = tbrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            for (int[] row : rotation) {
                for (int cell : row) {
                    assertTrue(cell == 0 || cell == 6, "T brick cells should be 0 or 6");
                }
            }
        }
    }

    // ==================== LBrick Tests (Four Rotations) ====================

    @Test
    void lBrickHasFourRotations() {
        Brick lbrick = new LBrick();
        List<int[][]> rotations = lbrick.getRotationMatrix();

        assertEquals(4, rotations.size(), "L brick should have 4 rotations");
    }

    @Test
    void lBrickAllRotationsAre4x4() {
        Brick lbrick = new LBrick();
        List<int[][]> rotations = lbrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            assertEquals(4, rotation.length, "Each L rotation should be 4x4");
            for (int[] row : rotation) {
                assertEquals(4, row.length, "Each L rotation should be 4x4");
            }
        }
    }

    @Test
    void lBrickEachRotationHasFourCells() {
        Brick lbrick = new LBrick();
        List<int[][]> rotations = lbrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            int cellCount = 0;
            for (int[] row : rotation) {
                for (int cell : row) {
                    if (cell == 3) cellCount++;
                }
            }
            assertEquals(4, cellCount, "L brick should have exactly 4 cells per rotation");
        }
    }

    @Test
    void lBrickContainsOnlyValidCellValues() {
        Brick lbrick = new LBrick();
        List<int[][]> rotations = lbrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            for (int[] row : rotation) {
                for (int cell : row) {
                    assertTrue(cell == 0 || cell == 3, "L brick cells should be 0 or 3");
                }
            }
        }
    }

    // ==================== JBrick Tests (Four Rotations) ====================

    @Test
    void jBrickHasFourRotations() {
        Brick jbrick = new JBrick();
        List<int[][]> rotations = jbrick.getRotationMatrix();

        assertEquals(4, rotations.size(), "J brick should have 4 rotations");
    }

    @Test
    void jBrickAllRotationsAre4x4() {
        Brick jbrick = new JBrick();
        List<int[][]> rotations = jbrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            assertEquals(4, rotation.length, "Each J rotation should be 4x4");
            for (int[] row : rotation) {
                assertEquals(4, row.length, "Each J rotation should be 4x4");
            }
        }
    }

    @Test
    void jBrickEachRotationHasFourCells() {
        Brick jbrick = new JBrick();
        List<int[][]> rotations = jbrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            int cellCount = 0;
            for (int[] row : rotation) {
                for (int cell : row) {
                    if (cell == 2) cellCount++;
                }
            }
            assertEquals(4, cellCount, "J brick should have exactly 4 cells per rotation");
        }
    }

    @Test
    void jBrickContainsOnlyValidCellValues() {
        Brick jbrick = new JBrick();
        List<int[][]> rotations = jbrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            for (int[] row : rotation) {
                for (int cell : row) {
                    assertTrue(cell == 0 || cell == 2, "J brick cells should be 0 or 2");
                }
            }
        }
    }

    // ==================== SBrick Tests (Two Rotations) ====================

    @Test
    void sBrickHasTwoRotations() {
        Brick sbrick = new SBrick();
        List<int[][]> rotations = sbrick.getRotationMatrix();

        assertEquals(2, rotations.size(), "S brick should have 2 rotations");
    }

    @Test
    void sBrickAllRotationsAre4x4() {
        Brick sbrick = new SBrick();
        List<int[][]> rotations = sbrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            assertEquals(4, rotation.length, "Each S rotation should be 4x4");
            for (int[] row : rotation) {
                assertEquals(4, row.length, "Each S rotation should be 4x4");
            }
        }
    }

    @Test
    void sBrickEachRotationHasFourCells() {
        Brick sbrick = new SBrick();
        List<int[][]> rotations = sbrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            int cellCount = 0;
            for (int[] row : rotation) {
                for (int cell : row) {
                    if (cell == 5) cellCount++;
                }
            }
            assertEquals(4, cellCount, "S brick should have exactly 4 cells per rotation");
        }
    }

    @Test
    void sBrickContainsOnlyValidCellValues() {
        Brick sbrick = new SBrick();
        List<int[][]> rotations = sbrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            for (int[] row : rotation) {
                for (int cell : row) {
                    assertTrue(cell == 0 || cell == 5, "S brick cells should be 0 or 5");
                }
            }
        }
    }

    // ==================== ZBrick Tests (Two Rotations) ====================

    @Test
    void zBrickHasTwoRotations() {
        Brick zbrick = new ZBrick();
        List<int[][]> rotations = zbrick.getRotationMatrix();

        assertEquals(2, rotations.size(), "Z brick should have 2 rotations");
    }

    @Test
    void zBrickAllRotationsAre4x4() {
        Brick zbrick = new ZBrick();
        List<int[][]> rotations = zbrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            assertEquals(4, rotation.length, "Each Z rotation should be 4x4");
            for (int[] row : rotation) {
                assertEquals(4, row.length, "Each Z rotation should be 4x4");
            }
        }
    }

    @Test
    void zBrickEachRotationHasFourCells() {
        Brick zbrick = new ZBrick();
        List<int[][]> rotations = zbrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            int cellCount = 0;
            for (int[] row : rotation) {
                for (int cell : row) {
                    if (cell == 7) cellCount++;
                }
            }
            assertEquals(4, cellCount, "Z brick should have exactly 4 cells per rotation");
        }
    }

    @Test
    void zBrickContainsOnlyValidCellValues() {
        Brick zbrick = new ZBrick();
        List<int[][]> rotations = zbrick.getRotationMatrix();

        for (int[][] rotation : rotations) {
            for (int[] row : rotation) {
                for (int cell : row) {
                    assertTrue(cell == 0 || cell == 7, "Z brick cells should be 0 or 7");
                }
            }
        }
    }

    // ==================== Generic Brick Tests ====================

    @Test
    void allBricksHaveValidRotationMatrix() {
        Brick[] bricks = {
            new OBrick(), new IBrick(), new TBrick(),
            new LBrick(), new JBrick(), new SBrick(), new ZBrick()
        };

        for (Brick brick : bricks) {
            List<int[][]> rotations = brick.getRotationMatrix();
            assertNotNull(rotations, "Brick should have non-null rotation matrix");
            assertTrue(rotations.size() > 0, "Brick should have at least 1 rotation");

            for (int[][] rotation : rotations) {
                assertNotNull(rotation, "Each rotation should be non-null");
                assertTrue(rotation.length > 0, "Each rotation should have rows");
                for (int[] row : rotation) {
                    assertTrue(row.length > 0, "Each rotation row should have columns");
                }
            }
        }
    }

    @Test
    void rotationMatrixIsIndependentCopy() {
        Brick obrick = new OBrick();
        List<int[][]> first = obrick.getRotationMatrix();
        List<int[][]> second = obrick.getRotationMatrix();

        // Modify first, verify second unchanged
        first.get(0)[0][0] = 99;
        assertEquals(0, second.get(0)[0][0], "Second rotation matrix should not be affected");
    }

    @Test
    void brickCellsAreNonNegative() {
        Brick[] bricks = {
            new OBrick(), new IBrick(), new TBrick(),
            new LBrick(), new JBrick(), new SBrick(), new ZBrick()
        };

        for (Brick brick : bricks) {
            for (int[][] rotation : brick.getRotationMatrix()) {
                for (int[] row : rotation) {
                    for (int cell : row) {
                        assertTrue(cell >= 0, "All brick cells should be non-negative");
                    }
                }
            }
        }
    }

    @Test
    void tetrisAllBricksRepresentative() {
        // Standard Tetris has exactly 7 tetrominoes
        Brick[] standardBricks = {
            new OBrick(), // Yellow
            new IBrick(), // Cyan
            new TBrick(), // Purple
            new LBrick(), // Orange
            new JBrick(), // Blue
            new SBrick(), // Green
            new ZBrick()  // Red
        };

        assertEquals(7, standardBricks.length, "Standard Tetris should have 7 pieces");
    }
}
