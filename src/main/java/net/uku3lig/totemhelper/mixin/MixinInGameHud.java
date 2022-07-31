package net.uku3lig.totemhelper.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.uku3lig.totemhelper.TotemHelper;
import net.uku3lig.totemhelper.config.TotemDisplayConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Stream;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    private static final ItemStack TOTEM = new ItemStack(Items.TOTEM_OF_UNDYING);
    private static final Identifier ICONS = new Identifier("totemhelper", "gui/icons.png");

    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private ItemRenderer itemRenderer;
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    private final TotemDisplayConfig config = TotemHelper.getConfig().getDisplayConfig();

    private int getTotemCount(PlayerEntity player) {
        if (player == null) return 0;
        PlayerInventory inv = player.getInventory();
        return (int) Stream.concat(inv.main.stream(), inv.offHand.stream()).filter(TOTEM::isItemEqual).count();
    }

    private boolean shouldRenderBar() {
        int count = getTotemCount(client.player);
        return config.isColoredXpBar() && (count <= 10 || config.isAlwaysShowBar()) && count != 0;
    }

    @Inject(method = "renderStatusBars", at = @At("RETURN"))
    private void renderCounter(MatrixStack matrices, CallbackInfo ci) {
        if (client.player == null) return;
        if (!config.isEnabled()) return;
        TextRenderer textRenderer = client.textRenderer;

        int count = getTotemCount(client.player);
        if (count == 0) return;
        Text text = Text.of(String.valueOf(count));
        float length = textRenderer.getWidth(text);

        // TOP_LEFT
        int x = 5;
        int y = 5;
        float textX = x + 18f;
        float textY = y + textRenderer.fontHeight / 2f;

        switch (config.getPosition()) {
            case MIDDLE -> {
                x = scaledWidth / 2 - 8;
                y = scaledHeight - 38 - textRenderer.fontHeight;
                if (client.player.experienceLevel > 0) y -= 6;
                textX = (scaledWidth - length) / 2;
                textY = y + 18f - textRenderer.fontHeight;
            }
            case TOP_RIGHT -> {
                x = scaledWidth - 21;
                textX = x - length - 2;
            }
            case BOTTOM_LEFT -> {
                y = scaledHeight - 21;
                textY = y + textRenderer.fontHeight / 2f;
            }
            case BOTTOM_RIGHT -> {
                x = scaledWidth - 21;
                y = scaledHeight - 21;
                textX = x - length - 2;
                textY = y + textRenderer.fontHeight / 2f;
            }
        }

        matrices.push();
        if (config.isUseDefaultTotem()) {
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.setShaderTexture(0, ICONS);
            ((InGameHud) (Object) this).drawTexture(matrices, x, y, 0, 0, 16, 16);
        } else {
            itemRenderer.renderGuiItemIcon(TOTEM, x, y);
        }

        matrices.translate(0, 0, itemRenderer.zOffset + 200);
        textRenderer.drawWithShadow(matrices, text, textX, textY, TotemHelper.getTotemColor(config.isColors() ? count : 999));
        matrices.pop();
    }

    @Redirect(method = "renderExperienceBar", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerEntity;experienceProgress:F"))
    public float changeXpProgress(ClientPlayerEntity instance) {
        return shouldRenderBar() ? 1 : instance.experienceProgress;
    }

    @Redirect(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1))
    public void hideExperienceBar(InGameHud instance, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        if (shouldRenderBar()) {
            int argb = TotemHelper.getTotemColor(getTotemCount(client.player));
            RenderSystem.setShaderTexture(0, ICONS);
            RenderSystem.setShaderColor(((argb >> 16) & 0xFF) / 255f, ((argb >> 8) & 0xFF) / 255f, (argb & 0xFF) / 255f, 1);
            instance.drawTexture(matrices, x, y, 0, 16, 182, 5);

            RenderSystem.setShaderTexture(0, DrawableHelper.GUI_ICONS_TEXTURE);
            RenderSystem.setShaderColor(1, 1, 1, 1);
        } else {
            instance.drawTexture(matrices, x, y, u, v, width, height);
        }
    }
}
