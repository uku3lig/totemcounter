package net.uku3lig.totemcounter.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.totemcounter.config.TotemCounterConfig;
import net.uku3lig.ukulib.utils.Ukutils;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Stream;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Shadow @Final private MinecraftClient client;

    @Unique
    private int getCount(PlayerEntity player) {
        if (player == null) return 0;
        if (TotemCounterConfig.get().isShowPopCounter()) return TotemCounter.getPops().getOrDefault(player.getUuid(), 0);

        PlayerInventory inv = player.getInventory();
        return (int) Stream.concat(inv.main.stream(), inv.offHand.stream()).filter(i -> i.isOf(TotemCounter.TOTEM.getItem())).count();
    }

    @Unique
    private int getColor(int count) {
        if (!TotemCounterConfig.get().isDisplayColors()) return 0xFFFFFFFF;
        return TotemCounterConfig.get().isShowPopCounter() ? TotemCounter.getPopColor(count) : TotemCounter.getTotemColor(count);
    }

    @Unique
    private boolean shouldRenderBar() {
        int count = getCount(client.player);
        return TotemCounterConfig.get().isColoredXpBar() && (count <= 10 || TotemCounterConfig.get().isAlwaysShowBar()) && count != 0;
    }

    @Inject(method = "renderStatusBars", at = @At("RETURN"))
    private void renderCounter(DrawContext context, CallbackInfo ci) {
        if (client.player == null) return;
        if (!TotemCounterConfig.get().isDisplayEnabled()) return;
        TextRenderer textRenderer = client.textRenderer;

        int count = getCount(client.player);
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

        context.getMatrices().push();
        if (TotemCounterConfig.get().isUseDefaultTotem()) {
            context.drawTexture(TotemCounter.ICONS, x, y, 0, 0, 16, 16);
        } else {
            context.drawItem(TotemCounter.TOTEM, x, y);
        }

        context.getMatrices().translate(0, 0, 200);
        context.drawTextWithShadow(textRenderer, text, coords.x(), coords.y(), getColor(count));
        context.getMatrices().pop();
    }

    @ModifyExpressionValue(method = "renderExperienceBar", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerEntity;experienceProgress:F"))
    public float changeXpProgress(float original) {
        return shouldRenderBar() ? 1 : original;
    }

    @WrapOperation(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIII)V"))
    public void hideExperienceBar(DrawContext context, Identifier texture, int i, int j, int k, int l, int x, int y, int width, int height, Operation<Void> original) {
        if (shouldRenderBar()) {
            int argb = getColor(getCount(client.player));
            context.setShaderColor(((argb >> 16) & 0xFF) / 255f, ((argb >> 8) & 0xFF) / 255f, (argb & 0xFF) / 255f, 1);
            context.drawTexture(TotemCounter.ICONS, x, context.getScaledWindowHeight() - 32 + 3, 0, 16, 182, 5);
            context.setShaderColor(1, 1, 1, 1);
        } else {
            original.call(context, texture, i, j, k, l, x, y, width, height);
        }
    }
}
