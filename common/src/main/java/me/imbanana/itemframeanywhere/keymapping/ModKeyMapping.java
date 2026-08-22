package me.imbanana.itemframeanywhere.keymapping;

import com.mojang.blaze3d.platform.InputConstants;
import me.imbanana.itemframeanywhere.ItemFrameAnywhere;
import me.imbanana.itemframeanywhere.util.IPlayer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.function.Consumer;

public class ModKeyMapping {
    private static final KeyMapping gridSnapKey = new KeyMapping(
            "key." + ItemFrameAnywhere.MOD_ID + ".grid_snap",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_LALT,
            KeyMapping.Category.GAMEPLAY
    );

    public static void registerModKeys(Consumer<KeyMapping> registerer) {
        registerer.accept(gridSnapKey);
    }

    public static void updateKeyMapping() {
        IPlayer iPlayer = ((IPlayer) Minecraft.getInstance().player);

        if (iPlayer != null && gridSnapKey.isDown() != iPlayer.isGridSnapping()) {
            iPlayer.setGridSnap(gridSnapKey.isDown());
        }
    }
}
