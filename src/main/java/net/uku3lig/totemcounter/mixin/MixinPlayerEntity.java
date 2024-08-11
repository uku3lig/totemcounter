package net.uku3lig.totemcounter.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    public void appendCounterLunar(CallbackInfoReturnable<Text> cir) {
        if (FabricLoader.getInstance().isModLoaded("ichor")) {
            PlayerEntity self = (PlayerEntity) (Object) this;
            Text modified = TotemCounter.showPopsInText(self, cir.getReturnValue());
            cir.setReturnValue(modified);
        }
    }
}
