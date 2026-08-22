package me.imbanana.itemframeanywhere.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Helper {
    public static Vec3 alignEntity(IPlayer player, Vec3 pos, Direction dir, AABB size) {
        Vec3 alignedPos = player.isGridSnapping() ? snapWithBlockGrid(pos, dir) : pos;
        return player.isGridSnapping() ? offsetBasedOnSize(alignedPos, size) : alignedPos;
    }

    public static double getPixelAlignment() {
        return 1 / 16f;
    }

    public static double getEntityBlockOffset() {
        return 0.03125;
    }

    public static Vec3 alignWithPixel(Vec3 value, Direction direction) {
        return alignWithPixel(value.x(), value.y(), value.z(), direction);
    }

    public static Vec3 alignWithPixel(double x, double y, double z, Direction direction) {
        return new Vec3(alignWithPixel(x), alignWithPixel(y), alignWithPixel(z)).relative(direction, 0.001);
    }

    public static double alignWithPixel(double value) {
        return Math.signum(value) * Math.round(Math.abs(value) / getPixelAlignment()) * getPixelAlignment();
    }

    public static Vec3 snapWithBlockGrid(Vec3 pos, Direction direction) {
        double offset = 0.5f;

        double x = direction.getAxis() == Direction.Axis.X ? pos.x() : (Math.floor(pos.x()) + offset);
        double y = direction.getAxis() == Direction.Axis.Y ? pos.y() : (Math.floor(pos.y()) + offset);
        double z = direction.getAxis() == Direction.Axis.Z ? pos.z() : (Math.floor(pos.z()) + offset);

        return new Vec3(x, y, z);
    }

    public static Vec3 offsetBasedOnSize(Vec3 pos, AABB box) {
        double xOffset = box.getXsize() % 2 < 0.25 && box.getXsize() > 1 ? -0.5 : 0;
        double yOffset = box.getYsize() % 2 < 0.25 && box.getYsize() > 1 ? -0.5 : 0;
        double zOffset = box.getZsize() % 2 < 0.25 && box.getZsize() > 1 ? -0.5 : 0;

        return new Vec3(pos.x() + xOffset, pos.y() + yOffset, pos.z() + zOffset);
    }
}
