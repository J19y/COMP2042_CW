package com.comp2042.event;

import com.comp2042.model.ShowResult;

public interface DropInput {
    ShowResult onDown(MoveEvent event);
}
