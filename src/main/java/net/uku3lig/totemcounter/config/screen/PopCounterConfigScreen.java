package net.uku3lig.totemcounter.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.config.GlobalConfig;
import net.uku3lig.totemcounter.config.PopCounterConfig;

public class PopCounterConfigScreen extends AbstractConfigScreen {
    private final PopCounterConfig config;

    public PopCounterConfigScreen(Screen parent, GlobalConfig config) {
        super(parent, Text.translatable("totemcounter.config.pop"), config);
        this.config = config.getCounterConfig();
    }

    @Override
    protected SimpleOption<?>[] getOptions() {
        return new SimpleOption[] {
                SimpleOption.ofBoolean("totemcounter.config.enabled", config.isEnabled(), config::setEnabled),
                SimpleOption.ofBoolean("totemcounter.config.pop.separator", config.isSeparator(), config::setSeparator),
                SimpleOption.ofBoolean("totemcounter.config.colors", config.isColors(), config::setColors)
        };
    }
}
