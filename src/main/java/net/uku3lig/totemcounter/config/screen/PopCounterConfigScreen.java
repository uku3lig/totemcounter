package net.uku3lig.totemcounter.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.TranslatableText;
import net.uku3lig.totemcounter.config.GlobalConfig;
import net.uku3lig.totemcounter.config.PopCounterConfig;

public class PopCounterConfigScreen extends AbstractConfigScreen {
    private final PopCounterConfig config;

    public PopCounterConfigScreen(Screen parent, GlobalConfig config) {
        super(parent, new TranslatableText("totemcounter.config.pop"), config);
        this.config = config.getCounterConfig();
    }

    @Override
    protected Option[] getOptions() {
        return new Option[] {
                CyclingOption.create("totemcounter.config.enabled", opt -> config.isEnabled(), (opt, option, value) -> config.setEnabled(value)),
                CyclingOption.create("totemcounter.config.pop.separator", opt -> config.isSeparator(), (opt, option, value) -> config.setSeparator(value)),
                CyclingOption.create("totemcounter.config.colors", opt -> config.isColors(), (opt, option, value) -> config.setColors(value))
        };
    }
}
