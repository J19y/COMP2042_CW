package com.comp2042.tetris.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class ShowResultTest {

    @Test
    void constructorWithBothNonNull() {
        RowClearResult clearResult = new RowClearResult(1, new int[][]{{1}}, 50, null);
        ViewData viewData = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});

        ShowResult result = new ShowResult(clearResult, viewData);

        assertNotNull(result.getClearRow());
        assertNotNull(result.getViewData());
        assertSame(clearResult, result.getClearRow());
        assertSame(viewData, result.getViewData());
    }

    @Test
    void constructorWithNullClearResult() {
        ViewData viewData = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});

        ShowResult result = new ShowResult(null, viewData);

        assertNull(result.getClearRow());
        assertNotNull(result.getViewData());
        assertSame(viewData, result.getViewData());
    }

    @Test
    void constructorWithNullViewData() {
        RowClearResult clearResult = new RowClearResult(1, new int[][]{{1}}, 50, null);

        ShowResult result = new ShowResult(clearResult, null);

        assertNotNull(result.getClearRow());
        assertNull(result.getViewData());
        assertSame(clearResult, result.getClearRow());
    }

    @Test
    void constructorWithBothNull() {
        ShowResult result = new ShowResult(null, null);

        assertNull(result.getClearRow());
        assertNull(result.getViewData());
    }

    @Test
    void getClearRowReturnsExactInstance() {
        RowClearResult clearResult = new RowClearResult(2, new int[][]{{1}}, 200, null);
        ShowResult result = new ShowResult(clearResult, null);

        assertSame(clearResult, result.getClearRow(), "Should return exact instance, not copy");
    }

    @Test
    void getViewDataReturnsExactInstance() {
        ViewData viewData = new ViewData(new int[][]{{1}}, 5, 10, new int[][]{{0}});
        ShowResult result = new ShowResult(null, viewData);

        assertSame(viewData, result.getViewData(), "Should return exact instance, not copy");
    }

    @Test
    void multipleClearRowCallsReturnSameInstance() {
        RowClearResult clearResult = new RowClearResult(3, new int[][]{{1}}, 300, null);
        ShowResult result = new ShowResult(clearResult, null);

        assertSame(result.getClearRow(), result.getClearRow());
    }

    @Test
    void multipleViewDataCallsReturnSameInstance() {
        ViewData viewData = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});
        ShowResult result = new ShowResult(null, viewData);

        assertSame(result.getViewData(), result.getViewData());
    }

    @Test
    void zeroClearLines() {
        RowClearResult clearResult = new RowClearResult(0, new int[][]{{1}}, 0, null);
        ViewData viewData = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});

        ShowResult result = new ShowResult(clearResult, viewData);

        assertEquals(0, result.getClearRow().getLinesRemoved());
        assertEquals(0, result.getClearRow().getScoreBonus());
    }

    @Test
    void multipleClearedLines() {
        RowClearResult clearResult = new RowClearResult(4, new int[][]{{1}}, 1200, null);
        ViewData viewData = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});

        ShowResult result = new ShowResult(clearResult, viewData);

        assertEquals(4, result.getClearRow().getLinesRemoved());
        assertEquals(1200, result.getClearRow().getScoreBonus());
    }

    @Test
    void viewDataPositionPreserved() {
        RowClearResult clearResult = new RowClearResult(1, new int[][]{{1}}, 50, null);
        ViewData viewData = new ViewData(new int[][]{{1}}, 7, 14, new int[][]{{0}});

        ShowResult result = new ShowResult(clearResult, viewData);

        assertEquals(7, result.getViewData().getxPosition());
        assertEquals(14, result.getViewData().getyPosition());
    }

    @Test
    void noLinesNoViewDataScenario() {
        RowClearResult clearResult = new RowClearResult(0, new int[][]{{1}}, 0, null);

        ShowResult result = new ShowResult(clearResult, null);

        assertEquals(0, result.getClearRow().getLinesRemoved());
        assertNull(result.getViewData(), "No lines, no view update required");
    }

    @Test
    void linesWithViewDataUpdate() {
        RowClearResult clearResult = new RowClearResult(2, new int[][]{{1, 1}}, 200, null);
        ViewData viewData = new ViewData(new int[][]{{1}}, 3, 5, new int[][]{{0}});

        ShowResult result = new ShowResult(clearResult, viewData);

        assertEquals(2, result.getClearRow().getLinesRemoved());
        assertNotNull(result.getViewData());
    }

    @Test
    void complexScenarioMultipleLinesMultipleBricks() {
        int[][] matrix = new int[20][10];
        RowClearResult clearResult = new RowClearResult(4, matrix, 1200, null);

        int[][] brickData = {{1, 0}, {0, 1}};
        int[][] nextBrick = {{2, 2}};
        ViewData viewData = new ViewData(brickData, 5, 10, nextBrick);

        ShowResult result = new ShowResult(clearResult, viewData);

        assertEquals(4, result.getClearRow().getLinesRemoved());
        assertEquals(1200, result.getClearRow().getScoreBonus());
        assertEquals(5, result.getViewData().getxPosition());
        assertEquals(10, result.getViewData().getyPosition());
    }
}
