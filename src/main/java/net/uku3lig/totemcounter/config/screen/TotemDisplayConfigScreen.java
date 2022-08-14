package net.uku3lig.totemcounter.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.TranslatableText;
import net.uku3lig.totemcounter.config.GlobalConfig;
import net.uku3lig.totemcounter.config.TotemDisplayConfig;
import net.uku3lig.totemcounter.config.TotemDisplayConfig.Position;

public class TotemDisplayConfigScreen extends AbstractConfigScreen {
    private final TotemDisplayConfig config;

    public TotemDisplayConfigScreen(Screen parent, GlobalConfig config) {
        super(parent, new TranslatableText("totemcounter.config.display"), config);
        this.config = config.getDisplayConfig();
    }

    @Override
    protected Option[] getOptions() {
        return new Option[] {
                CyclingOption.create("totemcounter.config.enabled", opt -> config.isEnabled(), (opt, option, value) -> config.setEnabled(value)),
                CyclingOption.create("totemcounter.config.display.position", Position.values(), p -> new TranslatableText(p.getTranslationKey()),
                        opt -> config.getPosition(), (opt, option, value) -> config.setPosition(value)),
                CyclingOption.create("totemcounter.config.display.defaultTotem", opt -> config.isUseDefaultTotem(), (opt, option, value) -> config.setUseDefaultTotem(value)),
                CyclingOption.create("totemcounter.config.colors", opt -> config.isColors(), (opt, option, value) -> config.setColors(value)),
                CyclingOption.create("totemcounter.config.display.coloredXpBar", opt -> config.isColoredXpBar(), (opt, option, value) -> config.setColoredXpBar(value)),
                CyclingOption.create("totemcounter.config.display.alwaysShowBar", opt -> config.isAlwaysShowBar(), (opt, option, value) -> config.setAlwaysShowBar(value))
        };
    }
}
