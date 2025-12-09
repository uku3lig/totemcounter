package net.uku3lig.totemcounter.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.totemcounter.config.TotemCounterConfig;
import net.uku3lig.ukulib.utils.Ukutils;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinGui {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "renderPlayerHealth", at = @At("RETURN"))
    private void renderCounter(GuiGraphics graphics, CallbackInfo ci) {
        if (minecraft.player == null) return;
        if (!TotemCounterConfig.get().isDisplayEnabled()) return;
        Font textRenderer = minecraft.font;

        int count = TotemCounter.getCount(minecraft.player);
        if (count == 0) return;

        MutableComponent text = Component.literal(String.valueOf(count));
        if (TotemCounterConfig.get().isShowPopCounter()) text = Component.literal("-").append(text);

        int x = TotemCounterConfig.get().getX();
        int y = TotemCounterConfig.get().getY();

        if (x == -1 || y == -1) {
            x = graphics.guiWidth() / 2 - 8;
            y = graphics.guiHeight() - 38 - textRenderer.lineHeight;
            if (minecraft != null && minecraft.player != null && minecraft.player.experienceLevel > 0) y -= 6;
        }

        Vector2ic coords = Ukutils.getTextCoords(text, graphics.guiWidth(), textRenderer, x, y);

        graphics.pose().pushMatrix();
        if (TotemCounterConfig.get().isUseDefaultTotem()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TotemCounter.DEFAULT_TOTEM, x, y, 0, 0, 16, 16, 16, 16);
        } else {
            graphics.renderItem(TotemCounter.TOTEM, x, y);
        }

        graphics.drawString(textRenderer, text, coords.x(), coords.y(), TotemCounter.getColor(count));
        graphics.pose().popMatrix();
    }
}
