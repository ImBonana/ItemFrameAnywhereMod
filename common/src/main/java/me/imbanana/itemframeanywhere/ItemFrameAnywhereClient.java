package me.imbanana.itemframeanywhere;

import me.imbanana.itemframeanywhere.mixin.HangingEntityAccessor;
import me.imbanana.itemframeanywhere.mixin.PaintingAccessor;
import me.imbanana.itemframeanywhere.util.Helper;
import me.imbanana.itemframeanywhere.util.IPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;

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
        Direction direction = hitResult.getDirection();
        Vec3 hitPos = hitResult.getLocation();
        Vec3 alignedPos = ((IPlayer) minecraft.player).isGridSnapping() ? Helper.snapWithBlockGrid(hitPos, direction) : hitPos;
        BlockPos blockPos = hitResult.getBlockPos().relative(direction);

        HangingEntity entity = null;
        Set<AABB> boxes = new HashSet<>();

        if (player.mayUseItemAt(blockPos, direction, item)) {
            if (item.is(Items.PAINTING)) {
                if (direction.getAxis().isHorizontal()) {
                    boxes.addAll(getOptionalVariantsBox(player.level(), blockPos, alignedPos, direction));
                }
            } else if (item.is(Items.ITEM_FRAME)) {
                entity = new ItemFrame(player.level(), blockPos, direction);
            } else if (item.is(Items.GLOW_ITEM_FRAME)) {
                entity = new GlowItemFrame(player.level(), blockPos, direction);
            } else {
                return;
            }
        } else {
            return;
        }


        boolean fails = boxes.isEmpty();

        if (boxes.isEmpty()) {
            if (entity == null) {
                Vec3 position = Helper.alignWithPixel(alignedPos, direction).relative(direction, Helper.getEntityBlockOffset());
                Direction.Axis axis = direction.getAxis();
                double xSize = axis == Direction.Axis.X ? 0.0625 : 1;
                double ySize = axis == Direction.Axis.Y ? 0.0625 : 1;
                double zSize = axis == Direction.Axis.Z ? 0.0625 : 1;
                boxes.add(AABB.ofSize(position, xSize, ySize, zSize));
                fails = true;
            } else {
                entity.setPos(alignedPos);
                boxes.add(entity.getBoundingBox());
                fails = !entity.survives();
            }

        }

        int color = fails ? ARGB.color(255, 255, 0, 0) : ARGB.color(255, 255, 255, 255);

        for (AABB box : boxes) {
            Gizmos.cuboid(box, GizmoStyle.stroke(color, 4));
        }
    }

    public static Set<AABB> getOptionalVariantsBox(final Level level, final BlockPos blockPos, Vec3 pos, final Direction direction) {
        Set<AABB> aabbs = new HashSet<>();

        Painting candidate = PaintingAccessor.construct(level, blockPos);
        candidate.setPos(pos);

        boolean isGridSnapping = ((IPlayer) Minecraft.getInstance().player).isGridSnapping();

        List<Holder<PaintingVariant>> potentialVariants = new ArrayList<>();
        level.registryAccess().lookupOrThrow(Registries.PAINTING_VARIANT).getTagOrEmpty(PaintingVariantTags.PLACEABLE).forEach(potentialVariants::add);

        if (potentialVariants.isEmpty()) {
            return aabbs;
        }

        ((HangingEntityAccessor) candidate).invokeSetDirection(direction);
        potentialVariants.removeIf(variant -> {
            ((PaintingAccessor) candidate).invokeSetVariant(variant);
            if (isGridSnapping) {
                candidate.setPos(Helper.offsetBasedOnSize(pos, candidate.getBoundingBox()));
            }
            return !candidate.survives();
        });

        if (potentialVariants.isEmpty()) {
            return aabbs;
        }

        int largestPaintingAreaSize = potentialVariants.stream().mapToInt(value -> value.value().area()).max().orElse(0);
        potentialVariants.removeIf(variant -> variant.value().area() < largestPaintingAreaSize);

        if (potentialVariants.isEmpty()) {
            return aabbs;
        }

        for (Holder<PaintingVariant> variantHolder : potentialVariants) {
            ((PaintingAccessor) candidate).invokeSetVariant(variantHolder);
            if (isGridSnapping) {
                candidate.setPos(Helper.offsetBasedOnSize(pos, candidate.getBoundingBox()));
            }
            aabbs.add(candidate.getBoundingBox());
        }

        return aabbs;
    }
}
