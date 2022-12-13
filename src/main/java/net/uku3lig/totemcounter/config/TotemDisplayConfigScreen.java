package net.uku3lig.totemcounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.TranslatableText;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.SubConfigScreen;
import net.uku3lig.ukulib.utils.Ukutils;

public class TotemDisplayConfigScreen extends SubConfigScreen<TotemCounterConfig.TotemDisplayConfig, TotemCounterConfig> {

    public TotemDisplayConfigScreen(Screen parent, ConfigManager<TotemCounterConfig> manager) {
        super(parent, new TranslatableText("totemcounter.config.display"), manager, TotemCounterConfig::getDisplayConfig);
    }

    @Override
    protected Option[] getOptions(TotemCounterConfig.TotemDisplayConfig config) {
        return new Option[] {
                CyclingOption.create("totemcounter.config.enabled", opt -> config.isEnabled(), (opt, option, value) -> config.setEnabled(value)),
                Ukutils.createOpenButton("ukulib.position", parent -> new DisplayPositionSelectScreen(parent, config)),
                CyclingOption.create("totemcounter.config.display.defaultTotem", opt -> config.isUseDefaultTotem(), (opt, option, value) -> config.setUseDefaultTotem(value)),
                CyclingOption.create("totemcounter.config.colors", opt -> config.isColors(), (opt, option, value) -> config.setColors(value)),
                CyclingOption.create("totemcounter.config.display.coloredXpBar", opt -> config.isColoredXpBar(), (opt, option, value) -> config.setColoredXpBar(value)),
                CyclingOption.create("totemcounter.config.display.alwaysShowBar", opt -> config.isAlwaysShowBar(), (opt, option, value) -> config.setAlwaysShowBar(value)),
                CyclingOption.create("totemcounter.config.display.showPopCounter", opt -> config.isShowPopCounter(),  (opt, option, value) -> config.setShowPopCounter(value))
        };
    }
}
