package me.imbanana.itemframeanywhere.neoforge;

import me.imbanana.itemframeanywhere.ItemFrameAnywhere;
import me.imbanana.itemframeanywhere.ItemFrameAnywhereClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = ItemFrameAnywhere.MOD_ID, dist = Dist.CLIENT)
public class ItemFrameAnywhereClientNeoForge {
    public ItemFrameAnywhereClientNeoForge(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(this::renderPlacementEvent);
    }

    private void renderPlacementEvent(RenderLevelStageEvent.AfterTranslucentFeatures event) {
        ItemFrameAnywhereClient.renderPlaceLocationOutline();
    }
}
