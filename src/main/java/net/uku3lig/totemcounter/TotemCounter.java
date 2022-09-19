package net.uku3lig.totemcounter;

import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.uku3lig.totemcounter.config.TotemCounterConfig;
import net.uku3lig.ukulib.config.ConfigManager;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TotemCounter implements ModInitializer {
    @Getter
    private static final Map<UUID, Integer> pops = new HashMap<>();
    @Getter
    private static final KeyBinding resetCounter = new KeyBinding("totemcounter.reset", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F10, "Totemcounter");
    @Getter
    private static final File file = new File("./config/totemcounter.toml");
    @Getter
    private static final ConfigManager<TotemCounterConfig> manager = ConfigManager.create(TotemCounterConfig.class, "totemcounter");

    @Override
    public void onInitialize() {
        KeyBindingHelper.registerKeyBinding(resetCounter);
    }

    public static int getPopColor(int pops) {
        return switch (pops) {
            case 1, 2 -> 0xFF55FF55; // light green
            case 3, 4 -> 0xFF00AA00; // dark green
            case 5, 6 -> 0xFFFFFF55; // yellow
            case 7, 8 -> 0xFFFFAA00; // gold
            default -> 0xFFFF5555; // red
        };
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
