package net.uku3lig.totemhelper;

import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.minecraft.text.Style;
import net.uku3lig.totemhelper.config.GlobalConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.util.Formatting.*;

public class TotemHelper implements ModInitializer {
    @Getter
    protected static final Map<UUID, Integer> pops = new HashMap<>();
    @Getter
    protected static GlobalConfig config;
    @Getter
    private static final File file = new File("./config/totemhelper.toml");

    @Override
    public void onInitialize() {
        config = GlobalConfig.readConfig(file);
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
}
