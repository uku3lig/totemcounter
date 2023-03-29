package net.uku3lig.totemcounter.mixin;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EntityRenderer.class)
@Slf4j
public class MixinEntityRenderer {
    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    public void label(Args args) {
        if (TotemCounter.getManager().getConfig().isDisableArmorStands()) return;

        Entity entity = args.get(0);
        Text text = args.get(1);

        if (!(entity instanceof ArmorStandEntity)) return;
        if (!entity.world.isClient) return;
        if (text == null) return;

        final String stringText = text.getString();
        if (stringText.isBlank()) return;

        Text fixedText = text;
        for (PlayerEntity player : entity.world.getPlayers()) {
            int index = stringText.indexOf(player.getEntityName());
            if (isSurrounded(stringText, index, player.getEntityName().length())) continue;

            if (!player.isAlive()) TotemCounter.getPops().remove(entity.getUuid());
            fixedText = TotemCounter.showPopsInText(player, text);
        }

        args.set(1, fixedText);
    }

    private boolean isSurrounded(String stringText, int index, int length) {
        return index == -1 || // not found
                (index > 0 && Character.isLetterOrDigit(stringText.charAt(index - 1))) || // first char is alphanumeric
                (index + length < stringText.length() && Character.isLetterOrDigit(stringText.charAt(index + length)));
    }
}
