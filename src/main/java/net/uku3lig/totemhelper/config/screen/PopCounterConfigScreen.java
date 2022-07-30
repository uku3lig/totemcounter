package net.uku3lig.totemhelper.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.totemhelper.config.GlobalConfig;
import net.uku3lig.totemhelper.config.PopCounterConfig;

public class PopCounterConfigScreen extends AbstractConfigScreen {
    private final PopCounterConfig config;

    public PopCounterConfigScreen(Screen parent, GlobalConfig config) {
        super(parent, Text.translatable("totemhelper.config.pop"), config);
        this.config = config.getCounterConfig();
    }

    @Override
    protected SimpleOption<?>[] getOptions() {
        return new SimpleOption[] {
                SimpleOption.ofBoolean("totemhelper.config.enabled", config.isEnabled(), config::setEnabled),
                SimpleOption.ofBoolean("totemhelper.config.pop.separator", config.isSeparator(), config::setSeparator),
                SimpleOption.ofBoolean("totemhelper.config.colors", config.isColors(), config::setColors)
        };
    }
}
