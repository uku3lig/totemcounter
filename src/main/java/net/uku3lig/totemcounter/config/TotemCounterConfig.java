package net.uku3lig.totemcounter.config;

import lombok.*;
import net.uku3lig.ukulib.config.IConfig;
import net.uku3lig.ukulib.config.ISubConfig;

@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class TotemCounterConfig implements IConfig<TotemCounterConfig> {
    private PopCounterConfig counterConfig;
    private TotemDisplayConfig displayConfig;
    private ExperimentalConfig experimentalConfig;

    @Override
    public TotemCounterConfig defaultConfig() {
        return new TotemCounterConfig(new PopCounterConfig().defaultConfig(), new TotemDisplayConfig().defaultConfig(), new ExperimentalConfig().defaultConfig());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TotemDisplayConfig implements ISubConfig<TotemDisplayConfig, TotemCounterConfig> {
        private boolean enabled;
        private int x = -1;
        private int y = -1;
        private boolean useDefaultTotem;
        private boolean colors;
        private boolean coloredXpBar;
        private boolean alwaysShowBar;
        private boolean showPopCounter;

        @Override
        public TotemDisplayConfig defaultConfig() {
            return new TotemDisplayConfig(true, -1, -1, false, true, false, false, false);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PopCounterConfig implements ISubConfig<PopCounterConfig, TotemCounterConfig> {
        private boolean enabled;
        private boolean separator;
        private boolean colors;
        private boolean showInTab;

        @Override
        public PopCounterConfig defaultConfig() {
            return new PopCounterConfig(true, true, true, false);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExperimentalConfig implements ISubConfig<ExperimentalConfig, TotemCounterConfig> {
        private boolean disableArmorStands;
        private double maxDistance;

        @Override
        public ExperimentalConfig defaultConfig() {
            return new ExperimentalConfig(false, 20);
        }
    }
}
