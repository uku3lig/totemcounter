package net.uku3lig.totemcounter.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;

public class GlobalConfigScreen extends AbstractConfigScreen<TotemCounterConfig> {
    public GlobalConfigScreen(Screen parent, ConfigManager<TotemCounterConfig> manager) {
        super(parent, Text.translatable("totemcounter.config"), manager);
    }

    @Override
    protected void init() {
        MinecraftClient.getInstance().setScreen(new TotemConfigScreen(this.parent));
    }

    @Override
    protected SimpleOption<?>[] getOptions(TotemCounterConfig config) {
        return new SimpleOption[] {};
    }
}
