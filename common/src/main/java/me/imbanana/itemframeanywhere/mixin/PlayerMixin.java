package me.imbanana.itemframeanywhere.mixin;

import me.imbanana.itemframeanywhere.network.ModNetworking;
import me.imbanana.itemframeanywhere.network.payloads.GridSnapPayloadC2S;
import me.imbanana.itemframeanywhere.util.IPlayer;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class PlayerMixin extends Avatar implements IPlayer, ContainerUser {

    @Unique
    private boolean gridSnap = false;

    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void setGridSnap(boolean gridSnap) {
        this.gridSnap = gridSnap;
        if (this.level().isClientSide()) {
            ModNetworking.sendC2S(new GridSnapPayloadC2S(gridSnap));
        }
    }

    @Override
    public boolean isGridSnapping() {
        return this.gridSnap;
    }
}
