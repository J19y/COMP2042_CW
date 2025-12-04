package com.comp2042.tetris.application.command;

import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.ui.input.MoveEvent;

public interface GameCommand {
    ShowResult execute(MoveEvent event);
}

