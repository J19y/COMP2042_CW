package com.comp2042.tetris.ui.input;

import com.comp2042.tetris.domain.model.ShowResult;

public interface MovementInput {
    ShowResult onLeft(MoveEvent event);
    ShowResult onRight(MoveEvent event);
    ShowResult onRotate(MoveEvent event);
}

