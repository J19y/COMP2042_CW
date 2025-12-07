package com.comp2042.tetris.ui.input;

/**
 * Enumeration of input event types for game control.
 * <p>
 * Defines all possible user input actions that can be performed
 * during gameplay.
 * </p>
 *
 * @version 1.0
 */
public enum EventType {
    /** Move brick down (soft drop) */
    DOWN,
    /** Move brick left */
    LEFT,
    /** Move brick right */
    RIGHT,
    /** Rotate brick clockwise */
    ROTATE,
    /** Instant drop to bottom (hard drop) */
    HARD_DROP,
    /** Pause/resume game */
    PAUSE
}

