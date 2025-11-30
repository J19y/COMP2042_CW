package com.comp2042.tetris.ui.input;

import com.comp2042.tetris.domain.model.ShowResult;

public interface DropInput {
    ShowResult onDown(MoveEvent event);
}

