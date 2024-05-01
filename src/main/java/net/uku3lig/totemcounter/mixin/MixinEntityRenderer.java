package net.uku3lig.totemcounter.mixin;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EntityRenderer.class)
@Slf4j
public class MixinEntityRenderer {
    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V"))
    public void label(Args args) {
        Entity entity = args.get(0);
        Text text = args.get(1);

        if (!entity.getWorld().isClient) return;
        if (text == null) return;

        final String stringText = text.getString();
        if (stringText.isBlank()) return;

        // try to get a player's uuid, sometimes by scanning the name of an armor stand
        PlayerEntity player = switch (entity) {
            case PlayerEntity p -> p;
            case ArmorStandEntity ignored -> {
                // TODO: maybe implement some sort of client-side cache for faster lookups? currently this being computed every frame lol
                for (PlayerEntity p : entity.getWorld().getPlayers()) {
                    int index = stringText.indexOf(p.getNameForScoreboard());
                    if (isSurrounded(stringText, index, p.getNameForScoreboard().length())) continue;

                    yield p;
                }
                yield null; // no matching player found
            }
            default -> null;
        };

        if (player == null) return;
        if (!player.isAlive()) TotemCounter.getPops().remove(entity.getUuid());

        Text fixedText = TotemCounter.showPopsInText(player, text);
        args.set(1, fixedText);
    }

    // 2024 edit: i have no fucking clue what this does but sure uku3lig from the past, slay queen
    @Unique
    private boolean isSurrounded(String stringText, int index, int length) {
        return index == -1 || // not found
                (index > 0 && Character.isLetterOrDigit(stringText.charAt(index - 1))) || // first char is alphanumeric
                (index + length < stringText.length() && Character.isLetterOrDigit(stringText.charAt(index + length)));
    }
}
