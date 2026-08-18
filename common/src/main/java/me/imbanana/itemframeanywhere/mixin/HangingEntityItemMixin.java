package me.imbanana.itemframeanywhere.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.imbanana.itemframeanywhere.ItemFrameAnywhere;
import me.imbanana.itemframeanywhere.util.MixinVarPass;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HangingEntityItem.class)
public abstract class HangingEntityItemMixin {
    @Inject(
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/EntityType;createDefaultStackConfig(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/entity/PostSpawnProcessor;"
            )
    )
    private void injectPos(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir, @Local HangingEntity entity) {
        entity.setPos(context.getClickLocation());
    }

    @Inject(
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/decoration/painting/Painting;create(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Ljava/util/Optional;"
            )
    )
    private void passClickPos(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        MixinVarPass.placeFromItem = true;
        MixinVarPass.clickPos = context.getClickLocation();
    }
}
