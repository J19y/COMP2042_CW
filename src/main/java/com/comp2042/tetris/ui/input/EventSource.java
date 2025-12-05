package com.comp2042.tetris.ui.input;

/**
 * Enumeration of event sources indicating the origin of game events.
 * <p>
 * Distinguishes between user-initiated actions and automatic game events.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public enum EventSource {
    /** Event triggered by user input (keyboard) */
    USER,
    /** Event triggered by game loop (auto-drop) */
    THREAD
}

