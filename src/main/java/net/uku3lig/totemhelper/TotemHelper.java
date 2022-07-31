package net.uku3lig.totemhelper;

import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Style;
import net.uku3lig.totemhelper.config.GlobalConfig;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.util.Formatting.*;

public class TotemHelper implements ModInitializer {
    @Getter
    private static final Map<UUID, Integer> pops = new HashMap<>();
    @Getter
    private static GlobalConfig config;
    @Getter
    private static KeyBinding resetCounter;
    @Getter
    private static final File file = new File("./config/totemhelper.toml");

    @Override
    public void onInitialize() {
        config = GlobalConfig.readConfig(file);
        resetCounter = KeyBindingHelper.registerKeyBinding(new KeyBinding("totemhelper.key.reset", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F10, "TotemHelper"));
    }

    public static Style getCounterStyle(int pops) {
        return Style.EMPTY.withColor(switch (pops) {
            case 1, 2 -> GREEN;
            case 3, 4 -> DARK_GREEN;
            case 5, 6 -> YELLOW;
            case 7, 8 -> GOLD;
            default -> RED;
        });
    }

    public static int getTotemColor(int amount) {
        return switch (amount) {
            case 1, 2 -> 0xFFFF5555; // red
            case 3, 4 -> 0xFFFFAA00; // gold
            case 5, 6 -> 0xFFFFFF55; // yellow
            case 7, 8 -> 0xFF00AA00; // dark green
            default -> 0xFF55FF55; // light green
        };
    }
}
