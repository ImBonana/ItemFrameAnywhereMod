package me.imbanana.itemframeanywhere;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ItemFrameAnywhere {
    public static final String MOD_ID = "item_frame_anywhere";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        // Write common init code here.
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
        return Math.signum(value) * Math.round(Math.abs(value) / ItemFrameAnywhere.getPixelAlignment()) * ItemFrameAnywhere.getPixelAlignment();
    }
}
