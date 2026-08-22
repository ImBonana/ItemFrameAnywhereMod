package me.imbanana.itemframeanywhere.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.imbanana.itemframeanywhere.util.Helper;
import me.imbanana.itemframeanywhere.util.MixinVarPass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

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
            painting.setPos(MixinVarPass.clickPos);
        }

        return painting;
    }

    @Inject(
            method = "lambda$create$0",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/decoration/painting/Painting;setVariant(Lnet/minecraft/core/Holder;)V",
                    shift = At.Shift.AFTER
            )
    )
    private static void fixPosition(Painting candidate, Holder variant, CallbackInfoReturnable<Boolean> cir) {
        if (MixinVarPass.placeFromItem) {
            candidate.setPos(MixinVarPass.gridSnap ? Helper.offsetBasedOnSize(MixinVarPass.clickPos, candidate.getBoundingBox()) : MixinVarPass.clickPos);
        }
    }

    @Inject(
            method = "create",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/decoration/painting/Painting;setVariant(Lnet/minecraft/core/Holder;)V",
                    shift = At.Shift.AFTER
            )
    )
    private static void fixPosition(Level level, BlockPos pos, Direction direction, CallbackInfoReturnable<Optional<Painting>> cir, @Local Painting candidate) {
        if (MixinVarPass.placeFromItem) {
            MixinVarPass.placeFromItem = false;
            candidate.setPos(MixinVarPass.gridSnap ? Helper.offsetBasedOnSize(MixinVarPass.clickPos, candidate.getBoundingBox()) : MixinVarPass.clickPos);
        }
    }

    @ModifyReturnValue(
            method = "trackingPosition",
            at = @At("RETURN")
    )
    private Vec3 modifyTrackingPos(Vec3 original) {
        return this.position();
    }
}
