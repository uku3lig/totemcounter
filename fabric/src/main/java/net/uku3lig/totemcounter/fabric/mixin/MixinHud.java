package net.uku3lig.totemcounter.fabric.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class MixinHud {
    @Inject(method = "extractPlayerHealth", at = @At("RETURN"))
    private void renderCounter(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        TotemCounter.renderTotemCounter(graphics);
    }
}
