package net.uku3lig.totemcounter.mixin;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.regex.Pattern;

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
        if (text == null || text.getString().isBlank()) return;

        final Text finalText = text; // i hate lambda

        Text fixedText = entity.world.getPlayers().stream()
                .filter(p -> finalText.getString().contains(p.getEntityName()))
                .filter(p -> finalText.getString().matches("(.*[^\\w\\n])?" + Pattern.quote(p.getEntityName()) + "(\\W.*)?"))
                .findFirst()
                .map(player -> {
                    if (!player.isAlive()) TotemCounter.getPops().remove(entity.getUuid());
                    return TotemCounter.showPopsInText(player, finalText);
                })
                .orElse(text);

        args.set(1, fixedText);
    }
}
