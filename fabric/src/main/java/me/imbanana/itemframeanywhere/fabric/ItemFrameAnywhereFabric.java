package me.imbanana.itemframeanywhere.fabric;

import me.imbanana.itemframeanywhere.ItemFrameAnywhere;
import net.fabricmc.api.ModInitializer;

public final class ItemFrameAnywhereFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        ItemFrameAnywhere.init();
    }
}
