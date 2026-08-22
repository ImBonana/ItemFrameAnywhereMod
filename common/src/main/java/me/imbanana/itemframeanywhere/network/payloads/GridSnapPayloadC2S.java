package me.imbanana.itemframeanywhere.network.payloads;

import me.imbanana.itemframeanywhere.ItemFrameAnywhere;
import me.imbanana.itemframeanywhere.util.IPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

public record GridSnapPayloadC2S(boolean state) implements CustomPacketPayload {
    public static final Identifier GRID_SNAP_PAYLOAD_ID = ItemFrameAnywhere.idOf("grid_snap");
    public static final CustomPacketPayload.Type<GridSnapPayloadC2S> TYPE = new CustomPacketPayload.Type<>(GRID_SNAP_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, GridSnapPayloadC2S> CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, GridSnapPayloadC2S::state, GridSnapPayloadC2S::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void receiveServer(GridSnapPayloadC2S payload, ServerPlayer player) {
        ((IPlayer) player).setGridSnap(payload.state());
    }
}
