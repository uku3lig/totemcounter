package net.uku3lig.popcounter;

import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.minecraft.text.Style;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.util.Formatting.*;

public class PopCounter implements ModInitializer {
    @Getter
    protected static final Map<UUID, Integer> pops = new HashMap<>();

    @Override
    public void onInitialize() {
        // soon:tm:
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
