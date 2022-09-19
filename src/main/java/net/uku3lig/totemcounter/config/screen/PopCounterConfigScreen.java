package net.uku3lig.totemcounter.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.config.GlobalConfig;
import net.uku3lig.totemcounter.config.PopCounterConfig;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.SubConfigScreen;

public class PopCounterConfigScreen extends SubConfigScreen<PopCounterConfig, GlobalConfig> {
    public PopCounterConfigScreen(Screen parent, ConfigManager<GlobalConfig> manager) {
        super(parent, Text.translatable("totemcounter.config.pop"), manager, GlobalConfig::getCounterConfig);
    }

    @Override
    protected SimpleOption<?>[] getOptions(PopCounterConfig config) {
        return new SimpleOption[] {
                SimpleOption.ofBoolean("totemcounter.config.enabled", config.isEnabled(), config::setEnabled),
                SimpleOption.ofBoolean("totemcounter.config.pop.separator", config.isSeparator(), config::setSeparator),
                SimpleOption.ofBoolean("totemcounter.config.colors", config.isColors(), config::setColors)
        };
    }
}
