package net.uku3lig.totemcounter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
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

@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "renderStatusBars", at = @At("RETURN"))
    private void renderCounter(DrawContext context, CallbackInfo ci) {
        if (client.player == null) return;
        if (!TotemCounterConfig.get().isDisplayEnabled()) return;
        TextRenderer textRenderer = client.textRenderer;

        int count = TotemCounter.getCount(client.player);
        if (count == 0) return;

        MutableText text = Text.literal(String.valueOf(count));
        if (TotemCounterConfig.get().isShowPopCounter()) text = Text.literal("-").append(text);

        int x = TotemCounterConfig.get().getX();
        int y = TotemCounterConfig.get().getY();

        if (x == -1 || y == -1) {
            x = context.getScaledWindowWidth() / 2 - 8;
            y = context.getScaledWindowHeight() - 38 - textRenderer.fontHeight;
            if (client != null && client.player != null && client.player.experienceLevel > 0) y -= 6;
        }

        Vector2ic coords = Ukutils.getTextCoords(text, context.getScaledWindowWidth(), textRenderer, x, y);

        context.getMatrices().pushMatrix();
        if (TotemCounterConfig.get().isUseDefaultTotem()) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, TotemCounter.DEFAULT_TOTEM, x, y, 0, 0, 16, 16, 16, 16);
        } else {
            context.drawItem(TotemCounter.TOTEM, x, y);
        }

        context.drawTextWithShadow(textRenderer, text, coords.x(), coords.y(), TotemCounter.getColor(count));
        context.getMatrices().popMatrix();
    }
}
