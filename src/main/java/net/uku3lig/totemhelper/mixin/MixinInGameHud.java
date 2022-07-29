package net.uku3lig.totemhelper.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
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

    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private ItemRenderer itemRenderer;

    @Inject(method = "renderStatusBars", at = @At("RETURN"))
    private void renderCounter(MatrixStack matrices, CallbackInfo ci) {
        if (client.player == null) return;
        TextRenderer textRenderer = client.textRenderer;

        int count = (int) client.player.getInventory().main.stream().filter(TOTEM::isItemEqual).count();
        if (count == 0) return;
        Text text = Text.of(String.valueOf(count));
        float length = textRenderer.getWidth(text);

        int x = client.getWindow().getScaledWidth() / 2 - 8;
        int y = client.getWindow().getScaledHeight() - 46 - textRenderer.fontHeight;

        itemRenderer.renderGuiItemIcon(TOTEM, x, y);
        matrices.translate(0, 0, itemRenderer.zOffset + 200);
        textRenderer.drawWithShadow(matrices, text, (client.getWindow().getScaledWidth() - length) / 2, y + 18f - textRenderer.fontHeight, Color.WHITE.getRGB());
    }
}
