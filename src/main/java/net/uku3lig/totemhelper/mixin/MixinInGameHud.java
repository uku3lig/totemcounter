package net.uku3lig.totemhelper.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    private static final ItemStack TOTEM = new ItemStack(Items.TOTEM_OF_UNDYING);
    private static final Identifier ICONS = new Identifier("totemhelper", "gui/icons.png");

    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private ItemRenderer itemRenderer;
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    @Inject(method = "renderStatusBars", at = @At("RETURN"))
    private void renderCounter(MatrixStack matrices, CallbackInfo ci) {
        if (client.player == null) return;
        TotemDisplayConfig config = TotemHelper.getConfig().getDisplayConfig();
        if (!config.isEnabled()) return;
        TextRenderer textRenderer = client.textRenderer;

        int count = (int) client.player.getInventory().main.stream().filter(TOTEM::isItemEqual).count();
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
                y = scaledHeight - 46 - textRenderer.fontHeight;
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
        textRenderer.drawWithShadow(matrices, text, textX, textY, Color.WHITE.getRGB());
        matrices.pop();
    }
}
