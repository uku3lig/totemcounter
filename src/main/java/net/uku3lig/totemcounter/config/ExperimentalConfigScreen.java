package net.uku3lig.totemcounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.SubConfigScreen;

public class ExperimentalConfigScreen extends SubConfigScreen<TotemCounterConfig.ExperimentalConfig, TotemCounterConfig> {
    protected ExperimentalConfigScreen(Screen parent, ConfigManager<TotemCounterConfig> manager) {
        super(parent, Text.of("Experimental Config"), manager, TotemCounterConfig::getExperimentalConfig);
    }

    @Override
    protected SimpleOption<?>[] getOptions(TotemCounterConfig.ExperimentalConfig config) {
        return new SimpleOption[]{
                SimpleOption.ofBoolean("Disable Armor Stands", config.isDisableArmorStands(), config::setDisableArmorStands),
                new SimpleOption<>("Armor Stand Distance", SimpleOption.emptyTooltip(), this::getDistanceText,
                        new SimpleOption.ValidatingIntSliderCallbacks(1, 100).withModifier(i -> (double) i, Double::intValue),
                        config.getMaxDistance(), config::setMaxDistance),
        };
    }

    private Text getDistanceText(Text prefix, double value) {
        return Text.of("%s: %.0f blocks".formatted(prefix.getString(), value));
    }
}
