package net.uku3lig.totemcounter.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerListHud.class)
public class MixinPlayerListHud {
    @ModifyReturnValue(method = "getPlayerName", at = @At("RETURN"))
    public Text addPopCounter(Text original, @Local(argsOnly = true) PlayerListEntry entry) {
        if (!TotemCounter.getManager().getConfig().isShowInTab()) return original;

        World world = MinecraftClient.getInstance().world;
        if (world != null) {
            PlayerEntity entity = world.getPlayerByUuid(entry.getProfile().getId());
            if (entity != null) {
                if (!entity.isAlive()) TotemCounter.getPops().remove(entity.getUuid());
                return TotemCounter.showPopsInText(entity, original);
            }
        }

        return original;
    }
}
