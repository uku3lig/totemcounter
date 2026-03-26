package net.uku3lig.totemcounter.config;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.ukulib.config.screen.PositionSelectScreen;
import net.uku3lig.ukulib.utils.Ukutils;
import org.joml.Vector2ic;

public class DisplayPositionSelectScreen extends PositionSelectScreen {
    private int ticksElapsed = 0;

    protected DisplayPositionSelectScreen(Screen parent, TotemCounterConfig config) {
        super("Position Select", parent, config.getX(), config.getY(), TotemCounter.getManager(), (x, y) -> {
            config.setX(x);
            config.setY(y);
        });
    }

    @Override
    public void tick() {
        this.ticksElapsed = (this.ticksElapsed + 1) % 100;
        super.tick();
    }

    @Override
    protected void draw(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta, int x, int y) {
        graphics.pose().pushMatrix();
        if (TotemCounter.getManager().getConfig().isUseDefaultTotem()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TotemCounter.DEFAULT_TOTEM, x, y, 0, 0, 16, 16, 16, 16);
        } else {
            graphics.item(TotemCounter.TOTEM.create(), x, y);
        }

        final Component exampleText = Component.nullToEmpty(String.valueOf(this.ticksElapsed / 4));
        final int color = TotemCounter.getTotemColor(this.ticksElapsed / 10);
        Vector2ic coords = Ukutils.getTextCoords(exampleText, this.width, font, x, y);

        graphics.text(this.font, exampleText, coords.x(), coords.y(), color);
        graphics.pose().popMatrix();
    }

    @Override
    protected void drawDefault(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        int x = width / 2 - 8;
        int y = height - 38 - font.lineHeight;
        draw(graphics, mouseX, mouseY, delta, x, y);
    }
}
