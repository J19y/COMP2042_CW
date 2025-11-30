package com.comp2042.tetris.domain.model;


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

