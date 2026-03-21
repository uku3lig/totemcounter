package net.uku3lig.totemcounter.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.client.renderer.entity.state.TextDisplayEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.player.Player;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.ukulib.utils.Ukutils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(DisplayRenderer.TextDisplayRenderer.class)
public class MixinTextDisplayRenderer {
    // Replaces cachedInfo in the render state so that text positioning, background width,
    // and rendered contents all agree on the counter-appended line width
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Display$TextDisplay;Lnet/minecraft/client/renderer/entity/state/TextDisplayEntityRenderState;F)V",
            at = @At("RETURN"))
    private void totemcounter$injectCounter(Display.TextDisplay entity, TextDisplayEntityRenderState renderState, float partialTick, CallbackInfo ci) {
        if (renderState.cachedInfo == null) return;

        final ClientLevel world = Minecraft.getInstance().level;
        if (world == null) return;

        List<Display.TextDisplay.CachedLine> lines = renderState.cachedInfo.lines();
        for (int i = 0; i < lines.size(); i++) {
            final Display.TextDisplay.CachedLine line = lines.get(i);
            final Component lineText = Ukutils.getStyledText(line.contents());
            final String lineString = lineText.getString();
            if (lineString.isBlank()) continue;

            // TODO: maybe implement some sort of client-side cache for faster lookups? currently this being computed every frame lol
            for (Player player : world.players()) {
                int index = lineString.indexOf(player.getScoreboardName());
                if (!isSurrounded(lineString, index, player.getScoreboardName().length())) {
                    if (!player.isAlive()) {
                        TotemCounter.getPops().remove(player.getUUID());
                        return;
                    }

                    final Component modified = TotemCounter.showPopsInText(player, lineText);
                    if (modified == lineText) return; // no pops or counter disabled

                    final FormattedCharSequence modifiedSeq = modified.getVisualOrderText();
                    final int newLineWidth = Minecraft.getInstance().font.width(modified);

                    final List<Display.TextDisplay.CachedLine> newLines = new ArrayList<>(lines);
                    newLines.set(i, new Display.TextDisplay.CachedLine(modifiedSeq, newLineWidth));

                    final int newMaxWidth = newLines.stream()
                            .mapToInt(Display.TextDisplay.CachedLine::width)
                            .max().orElse(renderState.cachedInfo.width());

                    renderState.cachedInfo = new Display.TextDisplay.CachedInfo(newLines, newMaxWidth);
                    return;
                }
            }
        }
    }

    // 2024 edit: i have no fucking clue what this does but sure uku3lig from the past, slay queen
    @Unique
    private boolean isSurrounded(String stringText, int index, int length) {
        return index == -1 || // not found
                (index > 0 && Character.isLetterOrDigit(stringText.charAt(index - 1))) || // first char is alphanumeric
                (index + length < stringText.length() && Character.isLetterOrDigit(stringText.charAt(index + length)));
    }
}
