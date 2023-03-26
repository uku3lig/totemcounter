package net.uku3lig.totemcounter.config;

import lombok.*;
import net.uku3lig.ukulib.config.IConfig;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TotemCounterConfig implements IConfig<TotemCounterConfig> {
    // === TOTEM DISPLAY CONFIG ===
    private boolean displayEnabled = true;
    private int x = -1;
    private int y = -1;
    private boolean useDefaultTotem = false;
    private boolean displayColors = true;
    private boolean coloredXpBar = false;
    private boolean alwaysShowBar = false;
    private boolean showPopCounter = false;

    // === POP COUNTER CONFIG ===
    private boolean counterEnabled = true;
    private boolean separator = true;
    private boolean counterColors = true;
    private boolean showInTab = false;

    // === EXPERIMENTAL CONFIG ===
    private boolean disableArmorStands = false;
    private double maxDistance = 20;

    @Override
    public TotemCounterConfig defaultConfig() {
        return new TotemCounterConfig();
    }
}
