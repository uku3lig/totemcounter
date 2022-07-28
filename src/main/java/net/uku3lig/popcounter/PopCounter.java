package net.uku3lig.popcounter;

import lombok.Getter;
import net.fabricmc.api.ModInitializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PopCounter implements ModInitializer {
    @Getter
    protected static final Map<UUID, Integer> pops = new HashMap<>();

    @Override
    public void onInitialize() {

    }
}
