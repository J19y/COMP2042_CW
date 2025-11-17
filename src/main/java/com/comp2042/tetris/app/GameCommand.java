package com.comp2042.tetris.app;

import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.ui.input.MoveEvent;

// Command interface for game actions triggered by user input.
public interface GameCommand {
    ShowResult execute(MoveEvent event);
}
