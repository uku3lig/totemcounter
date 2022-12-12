package net.uku3lig.totemcounter.config;

import lombok.extern.log4j.Log4j2;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;
import net.uku3lig.ukulib.utils.Ukutils;

@Log4j2
public class GlobalConfigScreen extends AbstractConfigScreen<TotemCounterConfig> {
    public GlobalConfigScreen(Screen parent, ConfigManager<TotemCounterConfig> manager) {
        super(parent, Text.translatable("totemcounter.config"), manager);
    }

    @Override
    protected SimpleOption<?>[] getOptions(TotemCounterConfig config) {
        return new SimpleOption[] {
                Ukutils.createOpenButton("totemcounter.config.pop", parent -> new PopCounterConfigScreen(parent, manager)),
                Ukutils.createOpenButton("totemcounter.config.display", parent -> new TotemDisplayConfigScreen(parent, manager)),
                Ukutils.createButton("totemcounter.reset", parent -> TotemCounter.resetPopCounter())
        };
    }
}
