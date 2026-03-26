package net.uku3lig.totemcounter.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ExperienceBarRenderer;
import net.minecraft.resources.Identifier;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.totemcounter.config.TotemCounterConfig;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ExperienceBarRenderer.class)
public class MixinExperienceBarRenderer {
    @Unique
    private boolean shouldRenderBar() {
        int count = TotemCounter.getCount(Minecraft.getInstance().player);
        return TotemCounterConfig.get().isColoredXpBar() && (count <= 10 || TotemCounterConfig.get().isAlwaysShowBar()) && count != 0;
    }

    @ModifyExpressionValue(method = "extractBackground", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;experienceProgress:F", opcode = Opcodes.GETFIELD))
    public float changeXpProgress(float original) {
        return shouldRenderBar() ? 1 : original;
    }

    @WrapOperation(method = "extractBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIIIIII)V"))
    public void hideExperienceBar(GuiGraphicsExtractor graphics, RenderPipeline pipeline, Identifier sprite, int textureWidth, int textureHeight, int u, int v, int x, int y, int width, int height, Operation<Void> original) {
        if (shouldRenderBar()) {
            int argb = TotemCounter.getColor(TotemCounter.getCount(Minecraft.getInstance().player));
            graphics.blit(pipeline, TotemCounter.WHITE_BAR, x, y, 0, 0, 182, 5, 182, 5, argb);
        } else {
            original.call(graphics, pipeline, sprite, textureWidth, textureHeight, u, v, x, y, width, height);
        }
    }
}
