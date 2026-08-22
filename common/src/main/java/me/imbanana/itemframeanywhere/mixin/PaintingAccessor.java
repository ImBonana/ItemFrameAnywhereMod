package me.imbanana.itemframeanywhere.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Painting.class)
public interface PaintingAccessor {
    @Invoker("<init>")
    static Painting construct(final Level level, final BlockPos blockPos) {
        return null;
    }

    @Invoker("setVariant")
    void invokeSetVariant(final Holder<PaintingVariant> variant);
}
