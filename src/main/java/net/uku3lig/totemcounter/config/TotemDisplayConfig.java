package net.uku3lig.totemcounter.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.uku3lig.ukulib.config.ISubConfig;
import net.uku3lig.ukulib.config.Position;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TotemDisplayConfig implements ISubConfig<TotemDisplayConfig, GlobalConfig> {
    private boolean enabled;
    private Position position;
    private boolean useDefaultTotem;
    private boolean colors;
    private boolean coloredXpBar;
    private boolean alwaysShowBar;
    private boolean showPopCounter;

    @Override
    public TotemDisplayConfig defaultConfig() {
        return new TotemDisplayConfig(true, Position.MIDDLE, false, true, false, false, false);
    }
}
