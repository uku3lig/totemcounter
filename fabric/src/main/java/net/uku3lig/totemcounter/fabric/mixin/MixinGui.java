package net.uku3lig.totemcounter.fabric.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinGui {
    @Inject(method = "extractPlayerHealth", at = @At("RETURN"))
    private void renderCounter(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        TotemCounter.renderTotemCounter(graphics);
    }
}
