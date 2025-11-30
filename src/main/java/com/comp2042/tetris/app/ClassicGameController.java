package com.comp2042.tetris.app;

import com.comp2042.tetris.mechanics.board.GameView;
import com.comp2042.tetris.services.audio.MusicManager;


public class ClassicGameController extends BaseGameController {
    public ClassicGameController(GameView view) {
        super(view);
    }

    @Override
    protected void onStart() {
        try {
            MusicManager.getInstance().playTrack(MusicManager.Track.CLASSIC, 900);
        } catch (Exception ignored) {}
    }
}

