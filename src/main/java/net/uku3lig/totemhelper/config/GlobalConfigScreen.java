package net.uku3lig.totemhelper.config;

import lombok.extern.log4j.Log4j2;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.uku3lig.totemhelper.TotemHelper;

import java.io.IOException;

@Log4j2
public class GlobalConfigScreen extends GameOptionsScreen {
    private final GlobalConfig config;

    public GlobalConfigScreen(Screen parent, GlobalConfig config) {
        super(parent, MinecraftClient.getInstance().options, Text.translatable("totemhelper.config"));
        this.config = config;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height / 2 - 10, 150, 20,
                Text.translatable("totemhelper.config.pop"), button -> client.setScreen(new PopCounterConfigScreen(this, config))));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height / 2 - 10, 150, 20,
                Text.translatable("totemhelper.config.display"), button -> client.setScreen(new TotemDisplayConfigScreen(this, config))));
    }

    @Override
    public void removed() {
        try {
            config.writeConfig(TotemHelper.getFile());
        } catch (IOException e) {
            log.warn("Could not write config", e);
        }
    }
}
