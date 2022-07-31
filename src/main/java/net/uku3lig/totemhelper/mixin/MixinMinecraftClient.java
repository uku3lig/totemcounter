package net.uku3lig.totemhelper.mixin;

import net.minecraft.client.MinecraftClient;
import net.uku3lig.totemhelper.TotemHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Inject(method = "tick", at = @At("RETURN"))
    public void processKeyBinds(CallbackInfo ci) {
        while (TotemHelper.getResetCounter().wasPressed()) {
            TotemHelper.getPops().clear();
        }
    }
}
