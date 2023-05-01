package net.uku3lig.totemcounter.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
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
    protected void draw(DrawContext drawContext, int mouseX, int mouseY, float delta, int x, int y) {
        drawContext.getMatrices().push();
        if (TotemCounter.getManager().getConfig().isUseDefaultTotem()) {
            drawContext.drawTexture(TotemCounter.ICONS, x, y, 0, 0, 16, 16);
        } else {
            drawContext.drawItem(TotemCounter.TOTEM, x, y);
        }

        final Text exampleText = Text.of(String.valueOf(this.ticksElapsed / 4));
        final int color = TotemCounter.getTotemColor(this.ticksElapsed / 10);
        Vector2ic coords = Ukutils.getTextCoords(exampleText, this.width, textRenderer, x, y);

        drawContext.getMatrices().translate(0, 0, 200);
        drawContext.drawTextWithShadow(this.textRenderer, exampleText, coords.x(), coords.y(), color);
        drawContext.getMatrices().pop();
    }

    @Override
    protected void drawDefault(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        int x = width / 2 - 8;
        int y = height - 38 - textRenderer.fontHeight;
        draw(drawContext, mouseX, mouseY, delta, x, y);
    }
}
