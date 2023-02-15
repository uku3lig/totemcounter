package net.uku3lig.totemcounter.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.CyclingOption;
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
        CyclingOption tabOption = TotemCounter.onOffOption("totemcounter.config.tab", config::isShowInTab, config::setShowInTab);
        tabOption.setTooltip(MinecraftClient.getInstance().textRenderer.wrapLines(new TranslatableText("totemcounter.experimental"), 200));

        return new Option[] {
                TotemCounter.onOffOption("totemcounter.config.enabled", config::isEnabled, config::setEnabled),
                TotemCounter.onOffOption("totemcounter.config.pop.separator", config::isSeparator, config::setSeparator),
                TotemCounter.onOffOption("totemcounter.config.colors", config::isColors, config::setColors),
                tabOption
        };
    }
}
