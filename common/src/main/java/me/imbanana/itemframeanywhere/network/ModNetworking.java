package me.imbanana.itemframeanywhere.network;

import me.imbanana.itemframeanywhere.network.payloads.GridSnapPayloadC2S;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

public class ModNetworking {
    private static Consumer<CustomPacketPayload> senderC2S;

    public static void registerClient(Consumer<CustomPacketPayload> sender) {
        ModNetworking.senderC2S = sender;
    }

    public static void registerC2S(C2SPayloadRegisterer registerer) {
        registerer.register(GridSnapPayloadC2S.TYPE, GridSnapPayloadC2S.CODEC, GridSnapPayloadC2S::receiveServer);
    }

    public static void sendC2S(CustomPacketPayload payload) {
        senderC2S.accept(payload);
    }

    @FunctionalInterface
    public interface C2SPayloadRegisterer {
        <T extends CustomPacketPayload> void register(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> codec, C2SPayloadHandler<T> handler);
    }

    @FunctionalInterface
    public interface C2SPayloadHandler<T extends CustomPacketPayload> {
        void handle(T payload, ServerPlayer player);
    }
}
