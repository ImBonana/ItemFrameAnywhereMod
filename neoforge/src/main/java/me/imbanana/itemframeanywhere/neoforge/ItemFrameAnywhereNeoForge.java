package me.imbanana.itemframeanywhere.neoforge;

import me.imbanana.itemframeanywhere.ItemFrameAnywhere;
import me.imbanana.itemframeanywhere.network.ModNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(ItemFrameAnywhere.MOD_ID)
public final class ItemFrameAnywhereNeoForge {
    private static PayloadRegistrar registrar;

    public ItemFrameAnywhereNeoForge(IEventBus modBus) {
        // Run our common setup.
        ItemFrameAnywhere.init();

        modBus.addListener(this::registerC2SPayload);
    }

    private void registerC2SPayload(RegisterPayloadHandlersEvent event) {
        registrar = event.registrar("1");
        ModNetworking.registerC2S(this::registerSingleC2S);
    }

    private <T extends CustomPacketPayload> void registerSingleC2S(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf,T> codec, ModNetworking.C2SPayloadHandler<T> handler) {
        registrar.playToServer(type, codec, (payload, iPayloadContext) -> handler.handle(payload, (ServerPlayer) iPayloadContext.player()));
    }
}
