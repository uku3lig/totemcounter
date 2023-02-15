package net.uku3lig.totemcounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.SubConfigScreen;

public class PopCounterConfigScreen extends SubConfigScreen<TotemCounterConfig.PopCounterConfig, TotemCounterConfig> {
    public PopCounterConfigScreen(Screen parent, ConfigManager<TotemCounterConfig> manager) {
        super(parent, new TranslatableText("totemcounter.config.pop"), manager, TotemCounterConfig::getCounterConfig);
    }

    @Override
    protected Option[] getOptions(TotemCounterConfig.PopCounterConfig config) {
        return new Option[] {
                CyclingOption.create("totemcounter.config.enabled", opt -> config.isEnabled(), (opt, option, value) -> config.setEnabled(value)),
                CyclingOption.create("totemcounter.config.pop.separator", opt -> config.isSeparator(), (opt, option, value) -> config.setSeparator(value)),
                CyclingOption.create("totemcounter.config.colors", opt -> config.isColors(), (opt, option, value) -> config.setColors(value)),
                CyclingOption.create("totemcounter.config.tab", SimpleOption.constantTooltip(Text.translatable("totemcounter.experimental")),
                        opt -> config.isShowInTab(), (opt, option, value) -> config.setShowInTab(value))
        };
    }
}
