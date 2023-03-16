package net.uku3lig.totemcounter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class MixinPlayerListHud {
    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    public void addPopCounter(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        if (!TotemCounter.getManager().getConfig().isShowInTab()) return;

        World world = MinecraftClient.getInstance().world;
        if (world != null) {
            PlayerEntity entity = world.getPlayerByUuid(entry.getProfile().getId());
            if (entity != null) {
                Text name = cir.getReturnValue();

                if (!entity.isAlive()) TotemCounter.getPops().remove(entity.getUuid());
                cir.setReturnValue(TotemCounter.showPopsInText(entity, name));
            }
        }
    }
}
