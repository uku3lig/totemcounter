package net.uku3lig.totemcounter.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
    @ModifyReturnValue(method = "getDisplayName", at = @At("RETURN"))
    public Text appendCounterLunar(Text original) {
        if (FabricLoader.getInstance().isModLoaded("ichor")) {
            PlayerEntity self = (PlayerEntity) (Object) this;
            return TotemCounter.showPopsInText(self, original);
        } else {
            return original;
        }
    }
}
