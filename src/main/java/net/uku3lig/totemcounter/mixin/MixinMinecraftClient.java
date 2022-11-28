package net.uku3lig.totemcounter.mixin;

import net.minecraft.client.MinecraftClient;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Inject(method = "tick", at = @At("RETURN"))
    public void processKeyBinds(CallbackInfo ci) {
        while (TotemCounter.getResetCounter().wasPressed()) {
            TotemCounter.resetPopCounter();
        }
    }
}
