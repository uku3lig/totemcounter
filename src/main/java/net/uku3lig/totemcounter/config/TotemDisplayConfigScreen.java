package net.uku3lig.totemcounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.Option;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.SubConfigScreen;
import net.uku3lig.ukulib.utils.Ukutils;

public class TotemDisplayConfigScreen extends SubConfigScreen<TotemCounterConfig.TotemDisplayConfig, TotemCounterConfig> {

    public TotemDisplayConfigScreen(Screen parent, ConfigManager<TotemCounterConfig> manager) {
        super(parent, Text.translatable("totemcounter.config.display"), manager, TotemCounterConfig::getDisplayConfig);
    }

    @Override
    protected Option<?>[] getOptions(TotemCounterConfig.TotemDisplayConfig config) {
        return new Option[] {
                Option.ofBoolean("totemcounter.config.enabled", config.isEnabled(), config::setEnabled),
                Ukutils.createOpenButton("ukulib.position", parent -> new DisplayPositionSelectScreen(parent, config)),
                Option.ofBoolean("totemcounter.config.display.defaultTotem", config.isUseDefaultTotem(), config::setUseDefaultTotem),
                Option.ofBoolean("totemcounter.config.colors", config.isColors(), config::setColors),
                Option.ofBoolean("totemcounter.config.display.coloredXpBar", config.isColoredXpBar(), config::setColoredXpBar),
                Option.ofBoolean("totemcounter.config.display.alwaysShowBar", config.isAlwaysShowBar(), config::setAlwaysShowBar),
                Option.ofBoolean("totemcounter.config.display.showPopCounter", config.isShowPopCounter(), config::setShowPopCounter)
        };
    }
}
