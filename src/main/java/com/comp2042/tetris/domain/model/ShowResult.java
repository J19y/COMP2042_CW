package com.comp2042.tetris.domain.model;

/**
 * Composite result object pairing a {@link RowClearResult} with current {@link ViewData}.
 * <p>
 * Returned from drop and move operations to provide both the visual state update
 * and any row clearing information to the UI layer.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public final class ShowResult {
    private final RowClearResult clearResult;
    private final ViewData viewData;

    /**
     * Constructs a ShowResult with clear result and view data.
     *
     * @param clearResult the row clear result (may be null if no clear occurred)
     * @param viewData the current view state data
     */
    public ShowResult(RowClearResult clearResult, ViewData viewData) {
        this.clearResult = clearResult;
        this.viewData = viewData;
    }

    /**
     * Gets the row clear result.
     *
     * @return the {@link RowClearResult}, or null if no rows were cleared
     */
    public RowClearResult getClearRow() {
        return clearResult;
    }

    /**
     * Gets the current view data.
     *
     * @return the {@link ViewData} containing brick position and shape information
     */
    public ViewData getViewData() {
        return viewData;
    }
}

