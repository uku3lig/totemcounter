package net.uku3lig.totemcounter.config;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.Position;
import net.uku3lig.ukulib.config.screen.SubConfigScreen;

import java.util.Arrays;

public class TotemDisplayConfigScreen extends SubConfigScreen<TotemCounterConfig.TotemDisplayConfig, TotemCounterConfig> {

    public TotemDisplayConfigScreen(Screen parent, ConfigManager<TotemCounterConfig> manager) {
        super(parent, Text.translatable("totemcounter.config.display"), manager, TotemCounterConfig::getDisplayConfig);
    }

    @Override
    protected SimpleOption<?>[] getOptions(TotemCounterConfig.TotemDisplayConfig config) {
        return new SimpleOption[] {
                SimpleOption.ofBoolean("totemcounter.config.enabled", config.isEnabled(), config::setEnabled),
                new SimpleOption<>("totemcounter.config.display.position", SimpleOption.emptyTooltip(), SimpleOption.enumValueText(),
                        new SimpleOption.PotentialValuesBasedCallbacks<>(Arrays.asList(Position.values()), Codec.STRING.xmap(Position::valueOf, Position::name)),
                        config.getPosition(), config::setPosition),
                SimpleOption.ofBoolean("totemcounter.config.display.defaultTotem", config.isUseDefaultTotem(), config::setUseDefaultTotem),
                SimpleOption.ofBoolean("totemcounter.config.colors", config.isColors(), config::setColors),
                SimpleOption.ofBoolean("totemcounter.config.display.coloredXpBar", config.isColoredXpBar(), config::setColoredXpBar),
                SimpleOption.ofBoolean("totemcounter.config.display.alwaysShowBar", config.isAlwaysShowBar(), config::setAlwaysShowBar),
                SimpleOption.ofBoolean("totemcounter.config.display.showPopCounter", config.isShowPopCounter(), config::setShowPopCounter)
        };
    }
}