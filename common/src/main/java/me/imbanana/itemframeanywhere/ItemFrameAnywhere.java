package me.imbanana.itemframeanywhere;

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
}
