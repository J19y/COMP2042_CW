package com.comp2042.tetris.domain.model;

/**
 * Renamed from DownData to ShowResult to better show this is a data object
 * returned to the UI with the outcome of a downward move (cleared rows + view data).
 */
public final class ShowResult {
    private final RowClearResult clearResult;
    private final ViewData viewData;

    public ShowResult(RowClearResult clearResult, ViewData viewData) {
        this.clearResult = clearResult;
        this.viewData = viewData;
    }

    public RowClearResult getClearRow() {
        return clearResult;
    }

    public ViewData getViewData() {
        return viewData;
    }
}
