package me.imbanana.itemframeanywhere.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.imbanana.itemframeanywhere.ItemFrameAnywhere;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockAttachedEntity.class)
public abstract class BlockAttachedEntityMixin extends Entity {
    public BlockAttachedEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @WrapMethod(
            method = "setPos"
    )
    private void injectSetPos(double x, double y, double z, Operation<Void> original) {
        Vec3 newPos = new Vec3(this.alignWithPixel(x), this.alignWithPixel(y), this.alignWithPixel(z)).relative(this.getDirection(), 0.001);
        this.setPosRaw(newPos.x(), newPos.y(), newPos.z());
        original.call(newPos.x(), newPos.y(), newPos.z());
    }

    @ModifyReturnValue(
            method = "repositionEntityAfterLoad",
            at = @At("RETURN")
    )
    private boolean fixEntityPositionAfterLoad(boolean original) {
        return original || ((Entity) this) instanceof HangingEntity;
    }

    @Unique
    private double alignWithPixel(double value) {
        return Math.signum(value) * Math.round(Math.abs(value) / ItemFrameAnywhere.getPixelAlignment()) * ItemFrameAnywhere.getPixelAlignment();
    }
}
