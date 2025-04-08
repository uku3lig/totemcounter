package net.uku3lig.totemcounter.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.DisplayEntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.ukulib.utils.Ukutils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DisplayEntityRenderer.TextDisplayEntityRenderer.class)
public class MixinTextDisplayEntityRenderer {
    @WrapOperation(method = "render(Lnet/minecraft/client/render/entity/state/TextDisplayEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/DisplayEntity$TextDisplayEntity$TextLine;contents()Lnet/minecraft/text/OrderedText;"))
    public OrderedText label(DisplayEntity.TextDisplayEntity.TextLine instance, Operation<OrderedText> original) {
        final Text text = Ukutils.getStyledText(instance.contents());
        final String stringText = text.getString();
        final ClientWorld world = MinecraftClient.getInstance().world;

        if (!stringText.isBlank() && world != null) {
            // TODO: maybe implement some sort of client-side cache for faster lookups? currently this being computed every frame lol
            for (PlayerEntity player : world.getPlayers()) {
                int index = stringText.indexOf(player.getNameForScoreboard());
                if (!isSurrounded(stringText, index, player.getNameForScoreboard().length())) {
                    if (!player.isAlive()) TotemCounter.getPops().remove(player.getUuid());
                    return TotemCounter.showPopsInText(player, text).asOrderedText();
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
