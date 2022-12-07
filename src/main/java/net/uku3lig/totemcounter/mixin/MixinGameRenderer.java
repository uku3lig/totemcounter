package net.uku3lig.totemcounter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Shadow @Final MinecraftClient client;

    @Inject(method = "showFloatingItem", at = @At("HEAD"))
    public void updateClientCounter(ItemStack floatingItem, CallbackInfo ci) {
        if (client.player == null) return;
        if (floatingItem.isOf(Items.TOTEM_OF_UNDYING)) {
            UUID uuid = client.player.getUuid();
            TotemCounter.getPops().putIfAbsent(uuid, 0);
            TotemCounter.getPops().computeIfPresent(uuid, (u, i) -> i + 1);
        }
    }
}
