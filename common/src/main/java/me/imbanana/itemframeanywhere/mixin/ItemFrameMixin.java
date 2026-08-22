package me.imbanana.itemframeanywhere.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.imbanana.itemframeanywhere.util.Helper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin extends HangingEntity {
    protected ItemFrameMixin(EntityType<? extends HangingEntity> type, Level level) {
        super(type, level);
    }

    @ModifyExpressionValue(
            method = "createBoundingBox",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;relative(Lnet/minecraft/core/Direction;D)Lnet/minecraft/world/phys/Vec3;")
    )
    private Vec3 fixBoundingBoxPos(Vec3 original, @Local(argsOnly = true) Direction direction) {
        return this.position().relative(direction, Helper.getEntityBlockOffset()); // copy the entity pos
    }

    @ModifyReturnValue(
            method = "getAddEntityPacket",
            at = @At("RETURN")
    )
    private Packet<ClientGamePacketListener> fixLoadPacket(Packet<ClientGamePacketListener> original, @Local(argsOnly = true) ServerEntity entity) {
        return new ClientboundAddEntityPacket(
                this.getId(),
                this.getUUID(),
                entity.getPositionBase().x(),
                entity.getPositionBase().y(),
                entity.getPositionBase().z(),
                this.getXRot(),
                this.getYRot(),
                this.getType(),
                this.getDirection().get3DDataValue(),
                this.getDeltaMovement(),
                this.getYHeadRot()
        );
    }

    @Inject(
            method = "survives",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/decoration/ItemFrame;level()Lnet/minecraft/world/level/Level;"
            ),
            cancellable = true
    )
    private void fixSupportCheck(CallbackInfoReturnable<Boolean> cir) {
        boolean isSupported = BlockPos.betweenClosedStream(this.calculateSupportBox()).allMatch(pos -> {
            BlockState state = this.level().getBlockState(pos);
            return state.isSolid() || DiodeBlock.isDiode(state);
        });

        cir.setReturnValue(isSupported && this.canCoexist(false));
    }
}
