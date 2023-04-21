package net.uku3lig.totemcounter.config;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.ukulib.config.screen.PositionSelectScreen;
import net.uku3lig.ukulib.utils.Ukutils;

public class DisplayPositionSelectScreen extends PositionSelectScreen {
    private int ticksElapsed = 0;

    protected DisplayPositionSelectScreen(Screen parent, TotemCounterConfig config) {
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
    protected void draw(DrawableHelper drawableHelper, int mouseX, int mouseY, float delta, int x, int y) {
        drawableHelper.method_51448().push();
        if (TotemCounter.getManager().getConfig().isUseDefaultTotem()) {
            drawableHelper.drawTexture(TotemCounter.ICONS, x, y, 0, 0, 16, 16);
        } else {
            drawableHelper.method_51427(TotemCounter.TOTEM, x, y);
        }

        final Text exampleText = Text.of(String.valueOf(this.ticksElapsed / 4));
        final int color = TotemCounter.getTotemColor(this.ticksElapsed / 10);
        Ukutils.Tuple2<Integer, Integer> coords = Ukutils.getTextCoords(exampleText, this.width, textRenderer, x, y);

        drawableHelper.method_51448().translate(0, 0, 200);
        drawableHelper.drawTextWithShadow(this.textRenderer, exampleText, coords.t1(), coords.t2(), color);
        drawableHelper.method_51448().pop();
    }

    @Override
    protected void drawDefault(DrawableHelper matrices, int mouseX, int mouseY, float delta) {
        int x = width / 2 - 8;
        int y = height - 38 - textRenderer.fontHeight;
        draw(matrices, mouseX, mouseY, delta, x, y);
    }
}
