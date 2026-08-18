package me.imbanana.itemframeanywhere.fabric;

import me.imbanana.itemframeanywhere.ItemFrameAnywhereClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;

public class ItemFrameAnywhereClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemFrameAnywhereClient.init();
        LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES.register((context) -> ItemFrameAnywhereClient.renderPlaceLocationOutline());
    }
}
