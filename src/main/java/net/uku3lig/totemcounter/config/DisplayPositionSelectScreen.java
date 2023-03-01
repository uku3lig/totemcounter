package net.uku3lig.totemcounter.config;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.ukulib.config.screen.PositionSelectScreen;
import net.uku3lig.ukulib.utils.Ukutils;

public class DisplayPositionSelectScreen extends PositionSelectScreen {
    private int ticksElapsed = 0;

    protected DisplayPositionSelectScreen(Screen parent, TotemCounterConfig.TotemDisplayConfig config) {
        super(Text.of("Position Select"), parent, config.getX(), config.getY(), TotemCounter.getManager(), (x, y) -> {
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
    protected void draw(MatrixStack matrices, int mouseX, int mouseY, float delta, int x, int y) {
        matrices.push();
        if (TotemCounter.getManager().getConfig().getDisplayConfig().isUseDefaultTotem()) {
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.setShaderTexture(0, TotemCounter.ICONS);
            drawTexture(matrices, x, y, 0, 0, 16, 16);
        } else {
            itemRenderer.renderGuiItemIcon(matrices, TotemCounter.TOTEM, x, y);
        }

        final Text exampleText = Text.of(String.valueOf(this.ticksElapsed / 4));
        final int color = TotemCounter.getTotemColor(this.ticksElapsed / 10);
        Ukutils.Tuple2<Integer, Integer> coords = Ukutils.getTextCoords(exampleText, this.width, textRenderer, x, y);

        matrices.translate(0, 0, 200);
        drawTextWithShadow(matrices, this.textRenderer, exampleText, coords.t1(), coords.t2(), color);
        matrices.pop();
    }

    @Override
    protected void drawDefault(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int x = width / 2 - 8;
        int y = height - 38 - textRenderer.fontHeight;
        draw(matrices, mouseX, mouseY, delta, x, y);
    }
}
