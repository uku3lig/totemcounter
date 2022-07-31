package net.uku3lig.totemcounter.config.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.config.GlobalConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public abstract class AbstractConfigScreen extends GameOptionsScreen {
    protected final Logger logger = LogManager.getLogger(getClass());
    protected final GlobalConfig globalConfig;
    protected ButtonListWidget buttonList;

    protected AbstractConfigScreen(Screen parent, Text title, GlobalConfig globalConfig) {
        super(parent, MinecraftClient.getInstance().options, title);
        this.globalConfig = globalConfig;
    }

    protected abstract SimpleOption<?>[] getOptions();

    @Override
    protected void init() {
        super.init();
        buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        buttonList.addAll(getOptions());
        this.addSelectableChild(buttonList);
        drawFooterButtons();
    }

    @SuppressWarnings("ConstantConditions")
    protected void drawFooterButtons() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.parent)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.buttonList.render(matrices, mouseX, mouseY, delta);
        DrawableHelper.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
        List<OrderedText> list = GameOptionsScreen.getHoveredButtonTooltip(this.buttonList, mouseX, mouseY);
        this.renderOrderedTooltip(matrices, list, mouseX, mouseY);
    }

    @Override
    public void removed() {
        try {
            globalConfig.writeConfig(net.uku3lig.totemcounter.TotemCounter.getFile());
        } catch (IOException e) {
            logger.warn("Could not save configuration file", e);
        }
    }
}
