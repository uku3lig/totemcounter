package net.uku3lig.totemcounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.Option;
import net.minecraft.text.TranslatableText;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;
import net.uku3lig.ukulib.utils.Ukutils;

public class GlobalConfigScreen extends AbstractConfigScreen<TotemCounterConfig> {
    public GlobalConfigScreen(Screen parent, ConfigManager<TotemCounterConfig> manager) {
        super(parent, new TranslatableText("totemcounter.config"), manager);
    }

    @Override
    protected Option[] getOptions(TotemCounterConfig config) {
        return new Option[] {
                Ukutils.createOpenButton("totemcounter.config.pop", parent -> new PopCounterConfigScreen(parent, manager)),
                Ukutils.createOpenButton("totemcounter.config.display", parent -> new TotemDisplayConfigScreen(parent, manager)),
                Ukutils.createOpenButton("Experimental Config", parent -> new ExperimentalConfigScreen(parent, manager)),
                Ukutils.createButton("totemcounter.reset", parent -> TotemCounter.resetPopCounter())
        };
    }
}
