package net.uku3lig.totemcounter.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
    @Shadow @Final
    private Minecraft minecraft;

    @Inject(method = "displayItemActivation", at = @At("HEAD"))
    public void updateClientCounter(ItemStack floatingItem, CallbackInfo ci) {
        if (minecraft.player == null) return;
        if (floatingItem.is(Items.TOTEM_OF_UNDYING)) {
            UUID uuid = minecraft.player.getUUID();
            TotemCounter.getPops().putIfAbsent(uuid, 0);
            TotemCounter.getPops().computeIfPresent(uuid, (_, i) -> i + 1);
        }
    }
}
