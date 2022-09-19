package net.uku3lig.totemcounter.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.uku3lig.ukulib.config.ISubConfig;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PopCounterConfig implements ISubConfig<PopCounterConfig, GlobalConfig> {
    private boolean enabled;
    private boolean separator;
    private boolean colors;

    @Override
    public PopCounterConfig defaultConfig() {
        return new PopCounterConfig(true, true, true);
    }
}
