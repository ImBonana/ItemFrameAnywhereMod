package me.imbanana.itemframeanywhere.neoforge;

import me.imbanana.itemframeanywhere.ItemFrameAnywhere;
import me.imbanana.itemframeanywhere.ItemFrameAnywhereClient;
import me.imbanana.itemframeanywhere.keymapping.ModKeyMapping;
import me.imbanana.itemframeanywhere.network.ModNetworking;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = ItemFrameAnywhere.MOD_ID, dist = Dist.CLIENT)
public class ItemFrameAnywhereClientNeoForge {
    public ItemFrameAnywhereClientNeoForge(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(this::renderPlacementEvent);
        NeoForge.EVENT_BUS.addListener(this::onClientTickEnd);

        modBus.addListener(this::registerModKeyMapping);

        ModNetworking.registerClient(ClientPacketDistributor::sendToServer);
    }

    private void renderPlacementEvent(RenderLevelStageEvent.AfterTranslucentFeatures event) {
        ItemFrameAnywhereClient.renderPlaceLocationOutline();
    }

    private void registerModKeyMapping(RegisterKeyMappingsEvent event) {
        ModKeyMapping.registerModKeys(event::register);
    }

    private void onClientTickEnd(ClientTickEvent.Post event) {
        ModKeyMapping.updateKeyMapping();
    }
}
