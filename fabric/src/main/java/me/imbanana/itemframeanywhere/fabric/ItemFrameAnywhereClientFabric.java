package me.imbanana.itemframeanywhere.fabric;

import me.imbanana.itemframeanywhere.ItemFrameAnywhereClient;
import me.imbanana.itemframeanywhere.keymapping.ModKeyMapping;
import me.imbanana.itemframeanywhere.network.ModNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;

public class ItemFrameAnywhereClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemFrameAnywhereClient.init();
        LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES.register((context) -> ItemFrameAnywhereClient.renderPlaceLocationOutline());
        ModKeyMapping.registerModKeys(KeyMappingHelper::registerKeyMapping);
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> ModKeyMapping.updateKeyMapping());
        ModNetworking.registerClient(ClientPlayNetworking::send);
    }
}
