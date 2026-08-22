package me.imbanana.itemframeanywhere.util;

import net.minecraft.world.phys.Vec3;

public class MixinVarPass {
    public static boolean placeFromItem = false;
    public static Vec3 clickPos = Vec3.ZERO;
    public static boolean gridSnap;
}
