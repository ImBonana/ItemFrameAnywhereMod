package me.imbanana.itemframeanywhere.fabric;

import me.imbanana.itemframeanywhere.ItemFrameAnywhere;
import me.imbanana.itemframeanywhere.network.ModNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public final class ItemFrameAnywhereFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ItemFrameAnywhere.init();

        ModNetworking.registerC2S(this::registerSingleC2S);
    }

    private <T extends CustomPacketPayload> void registerSingleC2S(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf,T> codec, ModNetworking.C2SPayloadHandler<T> handler) {
        PayloadTypeRegistry.serverboundPlay().register(type, codec);
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
           handler.handle(payload, context.player());
        });
    }
}
