package com.comp2042.tetris.engine.bricks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;


public final class BrickRegistry {

    private static final BrickRegistry INSTANCE = new BrickRegistry();

    private final List<Supplier<Brick>> suppliers = new ArrayList<>();

    private BrickRegistry() {
        registerInternal(IBrick::new);
        registerInternal(JBrick::new);
        registerInternal(LBrick::new);
        registerInternal(OBrick::new);
        registerInternal(SBrick::new);
        registerInternal(TBrick::new);
        registerInternal(ZBrick::new);
    }

    public static BrickRegistry getInstance() {
        return INSTANCE;
    }

    
    public static void register(Supplier<Brick> supplier) {
        INSTANCE.registerInternal(supplier);
    }

    private void registerInternal(Supplier<Brick> supplier) {
        if (supplier != null) {
            suppliers.add(supplier);
        }
    }

    public List<Supplier<Brick>> suppliers() {
        return Collections.unmodifiableList(suppliers);
    }

    
    public static List<Supplier<Brick>> getSuppliers() {
        return INSTANCE.suppliers();
    }
}

