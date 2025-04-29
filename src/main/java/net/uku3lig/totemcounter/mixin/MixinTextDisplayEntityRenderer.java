package net.uku3lig.totemcounter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.DisplayEntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DisplayEntityRenderer.TextDisplayEntityRenderer.class)
public class MixinTextDisplayEntityRenderer {
    @ModifyArg(method = "getLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;wrapLines(Lnet/minecraft/text/StringVisitable;I)Ljava/util/List;"), index = 0)
    public StringVisitable editText(StringVisitable original) {
        final Text text = (Text) original;
        final String stringText = text.getString();
        final ClientWorld world = MinecraftClient.getInstance().world;

        if (!stringText.isBlank() && world != null) {
            // TODO: maybe implement some sort of client-side cache for faster lookups? currently this being computed every frame lol
            for (PlayerEntity player : world.getPlayers()) {
                int index = stringText.indexOf(player.getNameForScoreboard());
                if (!isSurrounded(stringText, index, player.getNameForScoreboard().length())) {
                    if (!player.isAlive()) TotemCounter.getPops().remove(player.getUuid());
                    return TotemCounter.showPopsInText(player, text);
                }
            }
        }

        return original;
    }

    // 2024 edit: i have no fucking clue what this does but sure uku3lig from the past, slay queen
    @Unique
    private boolean isSurrounded(String stringText, int index, int length) {
        return index == -1 || // not found
                (index > 0 && Character.isLetterOrDigit(stringText.charAt(index - 1))) || // first char is alphanumeric
                (index + length < stringText.length() && Character.isLetterOrDigit(stringText.charAt(index + length)));
    }
}
