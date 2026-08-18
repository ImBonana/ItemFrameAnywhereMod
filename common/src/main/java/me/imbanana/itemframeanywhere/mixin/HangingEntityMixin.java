package me.imbanana.itemframeanywhere.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.imbanana.itemframeanywhere.ItemFrameAnywhere;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HangingEntity.class)
public abstract class HangingEntityMixin extends BlockAttachedEntity {
    protected HangingEntityMixin(EntityType<? extends BlockAttachedEntity> type, Level level) {
        super(type, level);
    }

    @WrapWithCondition(
            method = "recalculateBoundingBox",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/decoration/HangingEntity;setPosRaw(DDD)V"
            )
    )
    private boolean disablePosLocking(HangingEntity instance, double x, double y, double z) {
        return false;
    }

    @Inject(
            method = "onSyncedDataUpdated",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/decoration/HangingEntity;setDirection(Lnet/minecraft/core/Direction;)V"
            )
    )
    private void fixBlockOffset(EntityDataAccessor<?> accessor, CallbackInfo ci) {
        this.reapplyPosition();
    }

    @WrapOperation(
            method = "calculateSupportBox",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Vector3f;mul(F)Lorg/joml/Vector3f;"
            )
    )
    private Vector3f fixSupportBox(Vector3f instance, float scalar, Operation<Vector3f> original) {
        return original.call(instance, (float) - ItemFrameAnywhere.getEntityBlockOffset() * 2 - 0.001f);
    }
}
