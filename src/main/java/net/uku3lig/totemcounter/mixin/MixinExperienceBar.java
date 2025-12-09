package net.uku3lig.totemcounter.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.ExperienceBar;
import net.minecraft.util.Identifier;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.totemcounter.config.TotemCounterConfig;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ExperienceBar.class)
public class MixinExperienceBar {
    @Unique
    private boolean shouldRenderBar() {
        int count = TotemCounter.getCount(MinecraftClient.getInstance().player);
        return TotemCounterConfig.get().isColoredXpBar() && (count <= 10 || TotemCounterConfig.get().isAlwaysShowBar()) && count != 0;
    }

    @ModifyExpressionValue(method = "renderBar", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerEntity;experienceProgress:F", opcode = Opcodes.GETFIELD))
    public float changeXpProgress(float original) {
        return shouldRenderBar() ? 1 : original;
    }

    @WrapOperation(method = "renderBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIIIIIII)V"))
    public void hideExperienceBar(DrawContext context, RenderPipeline pipeline, Identifier sprite, int textureWidth, int textureHeight, int u, int v, int x, int y, int width, int height, Operation<Void> original) {
        if (shouldRenderBar()) {
            int argb = TotemCounter.getColor(TotemCounter.getCount(MinecraftClient.getInstance().player));
            context.drawTexture(pipeline, TotemCounter.WHITE_BAR, x, y, 0, 0, 182, 5, 182, 5, argb);
        } else {
            original.call(context, pipeline, sprite, textureWidth, textureHeight, u, v, x, y, width, height);
        }
    }
}
