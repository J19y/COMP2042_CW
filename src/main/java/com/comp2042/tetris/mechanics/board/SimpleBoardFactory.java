package com.comp2042.tetris.mechanics.board;

import com.comp2042.tetris.mechanics.piece.RandomBrickGeneratorFactory;
import java.util.Objects;


// Default factory that creates standard SimpleBoard instances.

public class SimpleBoardFactory implements BoardFactory {

    private final BoardComponentsFactory componentsFactory;

    public SimpleBoardFactory() {
        this(new DefaultBoardComponentsFactory(new RandomBrickGeneratorFactory()));
    }

    public SimpleBoardFactory(BoardComponentsFactory componentsFactory) {
        this.componentsFactory = Objects.requireNonNull(componentsFactory, "componentsFactory must not be null");
    }

    @Override
    public SimpleBoard create(int rows, int cols) {
        return new SimpleBoard(rows, cols, componentsFactory);
    }
}
