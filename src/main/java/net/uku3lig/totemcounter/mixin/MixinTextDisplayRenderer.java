package net.uku3lig.totemcounter.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.player.Player;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.ukulib.utils.Ukutils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DisplayRenderer.TextDisplayRenderer.class)
public class MixinTextDisplayRenderer {
    @WrapOperation(method = "submitInner(Lnet/minecraft/client/renderer/entity/state/TextDisplayEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IF)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;contents()Lnet/minecraft/util/FormattedCharSequence;"))
    public FormattedCharSequence label(Display.TextDisplay.CachedLine instance, Operation<FormattedCharSequence> original) {
        final Component text = Ukutils.getStyledText(instance.contents());
        final String stringText = text.getString();
        final ClientLevel world = Minecraft.getInstance().level;

        if (!stringText.isBlank() && world != null) {
            // TODO: maybe implement some sort of client-side cache for faster lookups? currently this being computed every frame lol
            for (Player player : world.players()) {
                int index = stringText.indexOf(player.getScoreboardName());
                if (!isSurrounded(stringText, index, player.getScoreboardName().length())) {
                    if (!player.isAlive()) TotemCounter.getPops().remove(player.getUUID());
                    return TotemCounter.showPopsInText(player, text).getVisualOrderText();
                }
            }
        }

        return instance.contents();
    }

    // 2024 edit: i have no fucking clue what this does but sure uku3lig from the past, slay queen
    @Unique
    private boolean isSurrounded(String stringText, int index, int length) {
        return index == -1 || // not found
                (index > 0 && Character.isLetterOrDigit(stringText.charAt(index - 1))) || // first char is alphanumeric
                (index + length < stringText.length() && Character.isLetterOrDigit(stringText.charAt(index + length)));
    }
}
