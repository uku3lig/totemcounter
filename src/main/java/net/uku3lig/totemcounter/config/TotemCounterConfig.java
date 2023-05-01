package net.uku3lig.totemcounter.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TotemCounterConfig implements Serializable {
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
}
