package com.comp2042.tetris.engine.bricks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Singleton registry for brick (tetromino) types.
 * <p>
 * Maintains a list of brick suppliers that can be used by generators
 * to create new brick instances. All standard Tetris bricks are
 * registered by default.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public final class BrickRegistry {

    private static final BrickRegistry INSTANCE = new BrickRegistry();

    private final List<Supplier<Brick>> suppliers = new ArrayList<>();

    /**
     * Private constructor initializes with all standard brick types.
     */
    private BrickRegistry() {
        registerInternal(IBrick::new);
        registerInternal(JBrick::new);
        registerInternal(LBrick::new);
        registerInternal(OBrick::new);
        registerInternal(SBrick::new);
        registerInternal(TBrick::new);
        registerInternal(ZBrick::new);
    }

    /**
     * Returns the singleton instance.
     *
     * @return the BrickRegistry instance
     */
    public static BrickRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Registers a custom brick supplier.
     *
     * @param supplier the supplier function to create brick instances
     */
    public static void register(Supplier<Brick> supplier) {
        INSTANCE.registerInternal(supplier);
    }

    private void registerInternal(Supplier<Brick> supplier) {
        if (supplier != null) {
            suppliers.add(supplier);
        }
    }

    /**
     * Returns an unmodifiable list of registered suppliers.
     *
     * @return the list of brick suppliers
     */
    public List<Supplier<Brick>> suppliers() {
        return Collections.unmodifiableList(suppliers);
    }

    /**
     * Static convenience method to get all suppliers.
     *
     * @return the list of brick suppliers
     */
    public static List<Supplier<Brick>> getSuppliers() {
        return INSTANCE.suppliers();
    }
}

