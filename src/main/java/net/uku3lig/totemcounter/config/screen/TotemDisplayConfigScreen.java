package net.uku3lig.totemcounter.config.screen;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.config.GlobalConfig;
import net.uku3lig.totemcounter.config.TotemDisplayConfig;
import net.uku3lig.totemcounter.config.TotemDisplayConfig.Position;

import java.util.Arrays;

public class TotemDisplayConfigScreen extends AbstractConfigScreen {
    private final TotemDisplayConfig config;

    public TotemDisplayConfigScreen(Screen parent, GlobalConfig config) {
        super(parent, Text.translatable("totemcounter.config.display"), config);
        this.config = config.getDisplayConfig();
    }

    @Override
    protected SimpleOption<?>[] getOptions() {
        return new SimpleOption[] {
                SimpleOption.ofBoolean("totemcounter.config.enabled", config.isEnabled(), config::setEnabled),
                new SimpleOption<>("totemcounter.config.display.position", SimpleOption.emptyTooltip(), SimpleOption.enumValueText(),
                        new SimpleOption.PotentialValuesBasedCallbacks<>(Arrays.asList(Position.values()), Codec.STRING.xmap(Position::valueOf, Position::name)),
                        config.getPosition(), config::setPosition),
                SimpleOption.ofBoolean("totemcounter.config.display.defaultTotem", config.isUseDefaultTotem(), config::setUseDefaultTotem),
                SimpleOption.ofBoolean("totemcounter.config.colors", config.isColors(), config::setColors),
                SimpleOption.ofBoolean("totemcounter.config.display.coloredXpBar", config.isColoredXpBar(), config::setColoredXpBar),
                SimpleOption.ofBoolean("totemcounter.config.display.alwaysShowBar", config.isAlwaysShowBar(), config::setAlwaysShowBar)
        };
    }
}
