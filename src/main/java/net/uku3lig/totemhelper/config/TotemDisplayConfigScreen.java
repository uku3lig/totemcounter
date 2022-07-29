package net.uku3lig.totemhelper.config;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.totemhelper.config.TotemDisplayConfig.Position;

import java.util.Arrays;

public class TotemDisplayConfigScreen extends AbstractConfigScreen {
    private final TotemDisplayConfig config;

    public TotemDisplayConfigScreen(Screen parent, GlobalConfig config) {
        super(parent, Text.translatable("totemhelper.config.display"), config);
        this.config = config.getDisplayConfig();
    }

    @Override
    protected SimpleOption<?>[] getOptions() {
        return new SimpleOption[] {
                SimpleOption.ofBoolean("totemhelper.config.enabled", config.isEnabled(), config::setEnabled),
                new SimpleOption<>("totemhelper.config.display.position", SimpleOption.emptyTooltip(), SimpleOption.enumValueText(),
                        new SimpleOption.PotentialValuesBasedCallbacks<>(Arrays.asList(Position.values()), Codec.STRING.xmap(Position::valueOf, Position::name)),
                        config.getPosition(), config::setPosition),
                SimpleOption.ofBoolean("totemhelper.config.display.defaultTotem", config.isUseDefaultTotem(), config::setUseDefaultTotem)
        };
    }
}
