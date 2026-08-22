package me.imbanana.itemframeanywhere.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.imbanana.itemframeanywhere.util.Helper;
import me.imbanana.itemframeanywhere.util.IPlayer;
import me.imbanana.itemframeanywhere.util.MixinVarPass;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;
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
        entity.setPos(Helper.alignEntity((IPlayer) context.getPlayer(), context.getClickLocation(), context.getClickedFace(), entity.getBoundingBox()));
    }

    @Inject(
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/decoration/painting/Painting;create(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Ljava/util/Optional;"
            )
    )
    private void passClickPos(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        boolean gridSnap = ((IPlayer) context.getPlayer()).isGridSnapping();

        Vec3 alignedPos = gridSnap ? Helper.snapWithBlockGrid(context.getClickLocation(), context.getClickedFace()) : context.getClickLocation();
        MixinVarPass.placeFromItem = true;
        MixinVarPass.clickPos = alignedPos;
        MixinVarPass.gridSnap = gridSnap;
    }
}
