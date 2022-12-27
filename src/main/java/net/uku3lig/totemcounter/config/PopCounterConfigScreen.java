package net.uku3lig.totemcounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.Option;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.SubConfigScreen;

public class PopCounterConfigScreen extends SubConfigScreen<TotemCounterConfig.PopCounterConfig, TotemCounterConfig> {
    public PopCounterConfigScreen(Screen parent, ConfigManager<TotemCounterConfig> manager) {
        super(parent, Text.translatable("totemcounter.config.pop"), manager, TotemCounterConfig::getCounterConfig);
    }

    @Override
    protected Option<?>[] getOptions(TotemCounterConfig.PopCounterConfig config) {
        return new Option[] {
                Option.ofBoolean("totemcounter.config.enabled", config.isEnabled(), config::setEnabled),
                Option.ofBoolean("totemcounter.config.pop.separator", config.isSeparator(), config::setSeparator),
                Option.ofBoolean("totemcounter.config.colors", config.isColors(), config::setColors)
        };
    }
}
