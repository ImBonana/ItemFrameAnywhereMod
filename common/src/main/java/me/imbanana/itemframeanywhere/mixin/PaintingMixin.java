package me.imbanana.itemframeanywhere.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.imbanana.itemframeanywhere.ItemFrameAnywhere;
import me.imbanana.itemframeanywhere.util.MixinVarPass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Painting.class)
public abstract class PaintingMixin extends HangingEntity {
    protected PaintingMixin(EntityType<? extends HangingEntity> type, Level level) {
        super(type, level);
    }

    @ModifyExpressionValue(
            method = "calculateBoundingBox",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;relative(Lnet/minecraft/core/Direction;D)Lnet/minecraft/world/phys/Vec3;")
    )
    private Vec3 fixBoundingBoxPos(Vec3 original, @Local(argsOnly = true) Direction direction) {
        return this.position().relative(direction, ItemFrameAnywhere.getEntityBlockOffset()); // copy the entity pos
    }

    @ModifyReturnValue(
            method = "getAddEntityPacket",
            at = @At("RETURN")
    )
    private Packet<ClientGamePacketListener> fixLoadPacket(Packet<ClientGamePacketListener> original, @Local(argsOnly = true) ServerEntity entity) {
        ItemFrameAnywhere.LOGGER.info(String.valueOf(entity.getPositionBase()));
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

    @WrapOperation(
            method = "create",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/entity/decoration/painting/Painting;"
            )
    )
    private static Painting fixPosition(Level level, BlockPos blockPos, Operation<Painting> original) {
        Painting painting = original.call(level, blockPos);

        if (MixinVarPass.placeFromItem) {
            MixinVarPass.placeFromItem = false;
            painting.setPos(MixinVarPass.clickPos);
        }

        return painting;
    }

    @ModifyReturnValue(
            method = "trackingPosition",
            at = @At("RETURN")
    )
    private Vec3 modifyTrackingPos(Vec3 original) {
        return this.position();
    }
}
