package me.imbanana.itemframeanywhere.mixin;

import me.imbanana.itemframeanywhere.util.Helper;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ItemFrameRenderer.class)
public abstract class ItemFrameRendererMixin {
    @ModifyConstant(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            constant = @Constant(doubleValue = 0.46875)
    )
    private double modifyOffset(double constant) {
        return constant + Helper.getEntityBlockOffset();
    }
}
