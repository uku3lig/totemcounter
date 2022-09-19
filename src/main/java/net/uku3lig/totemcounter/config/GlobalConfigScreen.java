package net.uku3lig.totemcounter.config;

import lombok.extern.log4j.Log4j2;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.ukulib.config.ConfigManager;

@Log4j2
public class GlobalConfigScreen extends GameOptionsScreen {
    private final ConfigManager<TotemCounterConfig> config;
    private ButtonListWidget buttons;

    public GlobalConfigScreen(Screen parent, ConfigManager<TotemCounterConfig> config) {
        super(parent, MinecraftClient.getInstance().options, Text.translatable("totemcounter.config"));
        this.config = config;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height / 2 - 35, 150, 20,
                Text.translatable("totemcounter.config.pop"), button -> client.setScreen(new PopCounterConfigScreen(this, config))));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height / 2 - 10, 150, 20,
                Text.translatable("totemcounter.config.display"), button -> client.setScreen(new TotemDisplayConfigScreen(this, config))));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height / 2 + 15, 150, 20, Text.translatable("totemcounter.reset"), button -> TotemCounter.getPops().clear()));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.parent)));

        buttons = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.addSelectableChild(buttons);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        buttons.render(matrices, mouseX, mouseY, delta);
        DrawableHelper.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void removed() {
        config.saveConfig();
    }
}