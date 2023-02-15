package net.uku3lig.totemcounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.SubConfigScreen;

public class PopCounterConfigScreen extends SubConfigScreen<TotemCounterConfig.PopCounterConfig, TotemCounterConfig> {
    public PopCounterConfigScreen(Screen parent, ConfigManager<TotemCounterConfig> manager) {
        super(parent, Text.translatable("totemcounter.config.pop"), manager, TotemCounterConfig::getCounterConfig);
    }

    @Override
    protected SimpleOption<?>[] getOptions(TotemCounterConfig.PopCounterConfig config) {
        return new SimpleOption[] {
                SimpleOption.ofBoolean("totemcounter.config.enabled", config.isEnabled(), config::setEnabled),
                SimpleOption.ofBoolean("totemcounter.config.pop.separator", config.isSeparator(), config::setSeparator),
                SimpleOption.ofBoolean("totemcounter.config.colors", config.isColors(), config::setColors),
                SimpleOption.ofBoolean("totemcounter.config.tab", SimpleOption.constantTooltip(Text.translatable("totemcounter.experimental")),
                        config.isShowInTab(), config::setShowInTab)
        };
    }
}
