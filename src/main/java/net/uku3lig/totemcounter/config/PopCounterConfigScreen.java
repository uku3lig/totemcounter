package net.uku3lig.totemcounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.Option;
import net.minecraft.text.TranslatableText;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.SubConfigScreen;

public class PopCounterConfigScreen extends SubConfigScreen<TotemCounterConfig.PopCounterConfig, TotemCounterConfig> {
    public PopCounterConfigScreen(Screen parent, ConfigManager<TotemCounterConfig> manager) {
        super(parent, new TranslatableText("totemcounter.config.pop"), manager, TotemCounterConfig::getCounterConfig);
    }

    @Override
    protected Option[] getOptions(TotemCounterConfig.PopCounterConfig config) {
        return new Option[] {
                TotemCounter.onOffOption("totemcounter.config.enabled", config::isEnabled, config::setEnabled),
                TotemCounter.onOffOption("totemcounter.config.pop.separator", config::isSeparator, config::setSeparator),
                TotemCounter.onOffOption("totemcounter.config.colors", config::isColors, config::setColors)
                CyclingOption.create("totemcounter.config.tab", new TranslatableText("totemcounter.experimental"),
                        opt -> config.isShowInTab(), (opt, option, value) -> config.setShowInTab(value))
        };
    }
}
