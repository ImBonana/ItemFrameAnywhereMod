package me.imbanana.itemframeanywhere;

import me.imbanana.itemframeanywhere.util.MixinVarPass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class ItemFrameAnywhereClient {
    public static void init() {

    }

    public static void renderPlaceLocationOutline() {
        Minecraft minecraft = Minecraft.getInstance();
        HitResult result = minecraft.hitResult;

        if (result == null || result.getType() != HitResult.Type.BLOCK) return;

        BlockHitResult hitResult = (BlockHitResult) result;
        LocalPlayer player = minecraft.player;
        ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
        BlockPos blockPos = hitResult.getBlockPos().relative(hitResult.getDirection());

        HangingEntity entity = null;

        if (player.mayUseItemAt(blockPos, hitResult.getDirection(), item)) {
            if (item.is(Items.PAINTING)) {
                if (hitResult.getDirection().getAxis().isHorizontal()) {
                    MixinVarPass.placeFromItem = true;
                    MixinVarPass.clickPos = hitResult.getLocation();
                    Optional<Painting> painting = Painting.create(player.level(), blockPos, hitResult.getDirection());

                    if (painting.isPresent()) {
                        entity = painting.get();
                    }
                }
            } else if (item.is(Items.ITEM_FRAME)) {
                entity = new ItemFrame(player.level(), blockPos, hitResult.getDirection());
            } else if (item.is(Items.GLOW_ITEM_FRAME)) {
                entity = new GlowItemFrame(player.level(), blockPos, hitResult.getDirection());
            } else {
                return;
            }
        } else {
            return;
        }

        AABB box;
        boolean fails;

        if (entity == null) {
            Vec3 position = ItemFrameAnywhere.alignWithPixel(hitResult.getLocation(), hitResult.getDirection()).relative(hitResult.getDirection(), ItemFrameAnywhere.getEntityBlockOffset());
            Direction.Axis axis = hitResult.getDirection().getAxis();
            double xSize = axis == Direction.Axis.X ? 0.0625 : 1;
            double ySize = axis == Direction.Axis.Y ? 0.0625 : 1;
            double zSize = axis == Direction.Axis.Z ? 0.0625 : 1;
            box = AABB.ofSize(position, xSize, ySize, zSize);
            fails = true;
        } else {
            entity.setPos(hitResult.getLocation());
            box = entity.getBoundingBox();
            fails = !entity.survives();
        }

        int color = fails ? ARGB.color(255, 255, 0, 0) : ARGB.color(255, 255, 255, 255);

        Gizmos.cuboid(box, GizmoStyle.stroke(color, 4));
    }
}
