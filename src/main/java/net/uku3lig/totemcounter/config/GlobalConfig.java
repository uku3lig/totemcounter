package net.uku3lig.totemcounter.config;

import lombok.*;
import net.uku3lig.ukulib.config.IConfig;

@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class GlobalConfig implements IConfig<GlobalConfig> {
    private PopCounterConfig counterConfig;
    private TotemDisplayConfig displayConfig;

    @Override
    public GlobalConfig defaultConfig() {
        return new GlobalConfig(new PopCounterConfig().defaultConfig(), new TotemDisplayConfig().defaultConfig());
    }
}
