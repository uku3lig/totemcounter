package net.uku3lig.totemcounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.SubConfigScreen;

public class ExperimentalConfigScreen extends SubConfigScreen<TotemCounterConfig.ExperimentalConfig, TotemCounterConfig> {
    protected ExperimentalConfigScreen(Screen parent, ConfigManager<TotemCounterConfig> manager) {
        super(parent, Text.of("Experimental Config"), manager, TotemCounterConfig::getExperimentalConfig);
    }

    @Override
    protected Option[] getOptions(TotemCounterConfig.ExperimentalConfig config) {
        return new Option[]{
                CyclingOption.create("Disable Armor Stands", opt -> config.isDisableArmorStands(), (opt, option, value) -> config.setDisableArmorStands(value)),
                new DoubleOption("Armor Stand Distance", 1, 100, 1, opt -> config.getMaxDistance(), (opt, value) -> config.setMaxDistance(value),
                        (opt, option) -> getDistanceText(config.getMaxDistance())),
        };
    }

    private Text getDistanceText(double value) {
        return Text.of("Armor Stand Distance: %.0f blocks".formatted(value));
    }
}
